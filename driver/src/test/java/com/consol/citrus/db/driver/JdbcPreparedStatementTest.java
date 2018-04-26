package com.consol.citrus.db.driver;

import org.apache.http.client.HttpClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class JdbcPreparedStatementTest {

    private HttpClient httpClientMock = mock(HttpClient.class);
    private JdbcConnection jdbcConnectionMock = mock(JdbcConnection.class);

    private JdbcPreparedStatement jdbcPreparedStatement = new JdbcPreparedStatement(httpClientMock,
                                                                                    "satement",
                                                                                    "url",
                                                                                    jdbcConnectionMock);

    @Test
    public void testSetNull() throws Exception {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setNull(0, 2);

        //THEN
        Assert.assertNull(jdbcPreparedStatement.getParameters().get(0));
    }

    @Test
    public void testSetNullOverwritesValue() throws Exception {

        //GIVEN

        //WHEN
        jdbcPreparedStatement.setNull(0, 2);
        jdbcPreparedStatement.setNull(0, 2);

        //THEN
        Assert.assertEquals(jdbcPreparedStatement.getParameters().size(), 1);
        Assert.assertNull(jdbcPreparedStatement.getParameters().get(0));
    }

}