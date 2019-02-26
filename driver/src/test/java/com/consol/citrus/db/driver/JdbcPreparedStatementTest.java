package com.consol.citrus.db.driver;

import com.consol.citrus.db.driver.dataset.DataSet;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.http.client.HttpClient;
import org.powermock.api.mockito.PowerMockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertTrue;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class JdbcPreparedStatementTest {

    private HttpClient httpClientMock = mock(HttpClient.class);
    private JdbcConnection jdbcConnectionMock = mock(JdbcConnection.class);

    private JdbcPreparedStatement jdbcPreparedStatement;

    @BeforeMethod
    public void setUp() {
        jdbcPreparedStatement = spy(new JdbcPreparedStatement(
                httpClientMock,
                "satement",
                "url",
                jdbcConnectionMock));
    }


    @Test
    public void testSetParameter() {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);

        //THEN
        Assert.assertEquals(jdbcPreparedStatement.getParameters().get("0"), 2);
    }

    @Test
    public void testSetParameterAddAnotherValue(){

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);
        jdbcPreparedStatement.setParameter(2, 42);

        //THEN
        Assert.assertEquals(jdbcPreparedStatement.getParameters().size(), 2);
        Assert.assertEquals(jdbcPreparedStatement.getParameters().get("0"), 2);
        Assert.assertEquals(jdbcPreparedStatement.getParameters().get("1"), 42);
    }

    @Test
    public void testSetParameterOverwritesValue() {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(1, 2);
        jdbcPreparedStatement.setParameter(1, 42);

        //THEN
        Assert.assertEquals(jdbcPreparedStatement.getParameters().size(), 1);
        Assert.assertEquals(jdbcPreparedStatement.getParameters().get("0"), 42);
    }

    @Test
    public void testAddParametersToBatch() throws SQLException {

        //GIVEN
        jdbcPreparedStatement.addBatch("SELECT id, name FROM cities WHERE abbreviation = ?");
        jdbcPreparedStatement.addBatch("SELECT id, name FROM airports WHERE name = ?");
        jdbcPreparedStatement.setParameter(1, "'MUC'");

        //WHEN
        jdbcPreparedStatement.addBatch();

        //THEN
        final List<String> batchStatements = jdbcPreparedStatement.getBatchStatements();
        assertTrue(batchStatements.contains("SELECT id, name FROM cities WHERE abbreviation = ? - ('MUC')"));
        assertTrue(batchStatements.contains("SELECT id, name FROM airports WHERE name = ? - ('MUC')"));
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