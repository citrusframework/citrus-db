package com.consol.citrus.db.driver;

import com.consol.citrus.db.driver.data.CitrusClob;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class JdbcPreparedStatementTest {

    private HttpClient httpClientMock = mock(HttpClient.class);
    private JdbcConnection jdbcConnectionMock = mock(JdbcConnection.class);

    private JdbcPreparedStatement jdbcPreparedStatement;

    @BeforeMethod
    public void setUp() {
        jdbcPreparedStatement = spy(new JdbcPreparedStatement(
                httpClientMock,
                "SELECT id, name FROM airports WHERE name = ?",
                "url",
                jdbcConnectionMock));
    }


    @Test
    public void testSetParameter() {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().get("0"), 2);
    }

    @Test
    public void testSetParameterAddAnotherValue(){

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);
        jdbcPreparedStatement.setParameter(2, 42);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().size(), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get("0"), 2);
        assertEquals(jdbcPreparedStatement.getParameters().get("1"), 42);
    }

    @Test
    public void testSetParameterOverwritesValue() {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);
        jdbcPreparedStatement.setParameter(1, 42);

        //THEN
        assertEquals(jdbcPreparedStatement.getParameters().size(), 1);
        assertEquals(jdbcPreparedStatement.getParameters().get("0"), 42);
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
    public void setLimitedClobFromReader() throws Exception {

        //GIVEN
        final StringReader stringReader = new StringReader("Stay positive, always!");
        final String expectedClobContent = "Stay positive";

        //WHEN
        jdbcPreparedStatement.setClob(5, stringReader, 13);

        //THEN
        final CitrusClob storedClob = (CitrusClob) jdbcPreparedStatement.getParameters().get("4");
        final String clobContent = IOUtils.toString(storedClob.getCharacterStream());
        assertEquals(clobContent, expectedClobContent);
    }

    @Test
    public void setClobFromReader() throws Exception {

        //GIVEN
        final String expectedClobContent = "Stay positive, always!";
        final StringReader stringReader = new StringReader(expectedClobContent);

        //WHEN
        jdbcPreparedStatement.setClob(5, stringReader);

        //THEN
        final CitrusClob storedClob = (CitrusClob) jdbcPreparedStatement.getParameters().get("4");
        final String clobContent = IOUtils.toString(storedClob.getCharacterStream());
        assertEquals(clobContent, expectedClobContent);
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(JdbcPreparedStatement.class).verify();
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
                .verify();
    }
}