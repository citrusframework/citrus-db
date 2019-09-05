package com.consol.citrus.db.driver.statement;

import com.consol.citrus.db.driver.JdbcConnection;
import com.consol.citrus.db.driver.JdbcResultSet;
import com.consol.citrus.db.driver.data.CitrusBlob;
import com.consol.citrus.db.driver.data.CitrusClob;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.statement.JdbcPreparedStatement;
import com.consol.citrus.db.driver.statement.JdbcStatement;
import com.consol.citrus.db.driver.utils.LobUtils;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.http.client.HttpClient;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class JdbcPreparedStatementTest {

    private final HttpClient httpClientMock = mock(HttpClient.class);
    private final JdbcConnection jdbcConnectionMock = mock(JdbcConnection.class);
    private LobUtils lobUtils;

    private JdbcPreparedStatement jdbcPreparedStatement;

    @BeforeMethod
    public void setUp() {
        lobUtils = PowerMockito.mock(LobUtils.class);
        jdbcPreparedStatement = spy(new JdbcPreparedStatement(
                httpClientMock,
                "SELECT id, name FROM airports WHERE name = ?",
                "url",
                jdbcConnectionMock,
                lobUtils));
    }


    @Test
    public void testSetParameter() {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().get(0), 2);
    }

    @Test
    public void testSetParameterAddAnotherValue(){

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);
        jdbcPreparedStatement.setParameter(2, 42);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().size(), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get(0), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get(1), 42);
    }

    @Test
    public void testSetParameterOverwritesValue() {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);
        jdbcPreparedStatement.setParameter(1, 42);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().size(), 1);
        assertEquals(jdbcPreparedStatement.getParameters().get(0), 42);
    }

    @Test
    public void testParameterOrderIsPreserved(){

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(2, 42);
        jdbcPreparedStatement.setParameter(1, 2);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().size(), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get(0), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get(1), 42);
    }

    @Test
    public void textExecuteBatchedPreparedStatements() throws SQLException {

        //GIVEN
        jdbcPreparedStatement.setString(1, "MUC");
        jdbcPreparedStatement.addBatch();

        jdbcPreparedStatement.setString(1, "DUS");
        jdbcPreparedStatement.addBatch();

        doReturn(false).when(jdbcPreparedStatement).execute(any());
        when(jdbcPreparedStatement.getUpdateCount()).thenReturn(42).thenReturn(84);

        final int[] expectedUpdateCounts = new int[]{42,84};

        //WHEN
        final int[] updateCounts = jdbcPreparedStatement.executeBatch();

        //THEN
        verify(jdbcPreparedStatement).execute("SELECT id, name FROM airports WHERE name = ? - (MUC)");
        verify(jdbcPreparedStatement).execute("SELECT id, name FROM airports WHERE name = ? - (DUS)");
        assertEquals(updateCounts, expectedUpdateCounts);
    }

    @Test(expectedExceptions = SQLException.class)
    public void textAddBatchWithSqlThrowsException() throws SQLException {

        //WHEN
        jdbcPreparedStatement.addBatch("some statement");

        //THEN
        //exception is thrown
    }

    @Test
    public void testSetClobWithParameterIndexAndClob() {

        //GIVEN
        final Clob clob = mock(Clob.class);

        //WHEN
        jdbcPreparedStatement.setClob(5, clob);

        //THEN
        verify(jdbcPreparedStatement).setParameter(5, clob);
    }

    @Test
    public void testSetLimitedClobFromReader() throws Exception {

        //GIVEN
        final long desiredLength = 13L;
        when(lobUtils.fitsInInt(desiredLength)).thenReturn(true);

        final CitrusClob expectedClob = mock(CitrusClob.class);
        final StringReader stringReaderMock = mock(StringReader.class);
        when(lobUtils.createClobFromReader(stringReaderMock, (int)desiredLength)).thenReturn(expectedClob);

        //WHEN
        jdbcPreparedStatement.setClob(12, stringReaderMock, desiredLength);

        //THEN
        verify(jdbcPreparedStatement).setParameter(12, expectedClob);
    }

    @Test
    public void TestNoopIfLengthExceedsInt() throws Exception {

        //GIVEN
        PowerMockito.when(lobUtils.fitsInInt(anyLong())).thenReturn(false);

        //WHEN
        jdbcPreparedStatement.setClob(12, PowerMockito.mock(Reader.class), Long.MAX_VALUE);

        //THEN
        verify(lobUtils, never()).createClobFromReader(any(Reader.class), anyInt());
    }

    @Test
    public void setClobFromReader() throws Exception {

        //GIVEN
        final Reader readerMock = mock(Reader.class);
        final CitrusClob citrusClobMock = mock(CitrusClob.class);
        when(lobUtils.createClobFromReader(readerMock, -1)).thenReturn(citrusClobMock);

        //WHEN
        jdbcPreparedStatement.setClob(5, readerMock);

        //THEN
        verify(jdbcPreparedStatement).setParameter(5, citrusClobMock);
    }

    @Test
    public void testSetBlobWithParameterIndexAndClob() {

        //GIVEN
        final Blob blob = mock(Blob.class);

        //WHEN
        jdbcPreparedStatement.setBlob(5, blob);

        //THEN
        verify(jdbcPreparedStatement).setParameter(5, blob);
    }

    @Test
    public void testSetLimitedBlobFromInputStream() throws Exception {

        //GIVEN
        final long desiredLength = 13L;
        when(lobUtils.fitsInInt(desiredLength)).thenReturn(true);

        final CitrusBlob expectedBlob = mock(CitrusBlob.class);
        final InputStream inputStreamMock = mock(InputStream.class);
        when(lobUtils.createBlobFromInputStream(inputStreamMock, (int)desiredLength)).thenReturn(expectedBlob);

        //WHEN
        jdbcPreparedStatement.setBlob(12, inputStreamMock, desiredLength);

        //THEN
        verify(jdbcPreparedStatement).setParameter(12, expectedBlob);
    }

    @Test
    public void setBlobFromInputStream() throws Exception {

        //GIVEN
        final InputStream inputStreamMock = mock(InputStream.class);
        final CitrusBlob citrusBlobMock = mock(CitrusBlob.class);
        when(lobUtils.createBlobFromInputStream(inputStreamMock, -1)).thenReturn(citrusBlobMock);

        //WHEN
        jdbcPreparedStatement.setBlob(5, inputStreamMock);

        //THEN
        verify(jdbcPreparedStatement).setParameter(5, citrusBlobMock);
    }

    @Test
    public void testParametersAreOrderedCorrectly() {

        //GIVEN
        final List<String> expectedParameter = Arrays.asList("foo", "bar");

        //WHEN
        jdbcPreparedStatement.setString(3,"foo");
        jdbcPreparedStatement.setString(11, "bar");

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().getParametersAsList(), expectedParameter);
    }

    @Test
    public void testToString(){
        ToStringVerifier
                .forClass(JdbcPreparedStatement.class)
                .withIgnoredFields("lobUtils", "statementComposer")//stateless
                .verify();
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(JdbcPreparedStatement.class)
                .withPrefabValues(
                        JdbcResultSet.class,
                        new JdbcResultSet(PowerMockito.mock(DataSet.class), PowerMockito.mock(JdbcStatement.class)),
                        new JdbcResultSet(PowerMockito.mock(DataSet.class), PowerMockito.mock(JdbcStatement.class)))
                .withRedefinedSuperclass()
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("resultSet")
                .withIgnoredFields("lobUtils")//stateless
                .withIgnoredFields("statementComposer")//stateless
                .verify();
    }
}