package com.consol.citrus.db.driver;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class JdbcConnectionTestIT {

    private static HttpClient httpClient = HttpClients.custom()
                                            .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT)
                                                    .build())
                                            .build();
    private static String serverUrl = "http://localhost:4567";
    private static JdbcConnection jdbcConnection = new JdbcConnection(httpClient, serverUrl);


    @Test
    public void testSetAndGetTransactionState() throws Exception{

        //requires a running citrus db instance
        assertFalse(jdbcConnection.getAutoCommit());

        jdbcConnection.setAutoCommit(true);
        assertTrue(jdbcConnection.getAutoCommit());

        jdbcConnection.setAutoCommit(false);
        assertFalse(jdbcConnection.getAutoCommit());
    }

    @Test
    public void testCommit() throws Exception{

        //requires a running citrus db instance
        jdbcConnection.commit();
    }

    @Test
    public void testRollback() throws Exception{

        //requires a running citrus db instance
        jdbcConnection.rollback();
    }
}