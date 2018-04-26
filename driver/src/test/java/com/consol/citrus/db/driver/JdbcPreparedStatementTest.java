package com.consol.citrus.db.driver;

import org.apache.http.client.HttpClient;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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
    public void testSetParameter() throws Exception {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(0, 2);

        //THEN
        Assert.assertEquals(jdbcPreparedStatement.getParameters().get(0), 2);
    }

    @Test
    public void testSetParameterAddAnotherValue() throws Exception {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(0, 2);
        jdbcPreparedStatement.setParameter(1, 42);

        //THEN
        Assert.assertEquals(jdbcPreparedStatement.getParameters().size(), 2);
        Assert.assertEquals(jdbcPreparedStatement.getParameters().get(0), 2);
        Assert.assertEquals(jdbcPreparedStatement.getParameters().get(1), 42);
    }

    @Test
    public void testSetParameterOverwritesValue() throws Exception {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setParameter(0, 2);
        jdbcPreparedStatement.setParameter(0, 42);

        //THEN
        Assert.assertEquals(jdbcPreparedStatement.getParameters().size(), 1);
        Assert.assertEquals(jdbcPreparedStatement.getParameters().get(0), 42);
    }
}