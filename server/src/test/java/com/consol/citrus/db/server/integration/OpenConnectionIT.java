/*
 * Copyright 2006-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.db.server.integration;

import com.consol.citrus.db.driver.JdbcDriver;
import com.consol.citrus.db.server.JdbcServer;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class OpenConnectionIT{

    private JdbcServer jdbcServer = new JdbcServer();
    private JdbcDriver jdbcDriver = new JdbcDriver();

    @BeforeSuite
    public void setUp(){
        jdbcServer.startAndAwaitInitialization();
    }

    @AfterSuite
    public void tearDown(){
        jdbcServer.stop();
    }

    @Test
    public void testOpenConnection() throws SQLException {

        //GIVEN
        final String url = "jdbc:citrus:localhost:4567";

        //WHEN
        final Connection connection = jdbcDriver.connect(url, new Properties());

        //THEN
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

}
