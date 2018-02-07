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
import com.consol.citrus.db.driver.data.Row;
import com.consol.citrus.db.driver.data.Table;
import com.consol.citrus.db.driver.dataset.TableDataSetProducer;
import com.consol.citrus.db.server.JdbcServer;
import com.consol.citrus.db.server.JdbcServerConfiguration;
import com.consol.citrus.db.server.controller.SimpleJdbcController;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
public class SimpleJdbcControllerIT {


    private JdbcServer jdbcServer;
    private Connection connection;
    private Statement statement;

    @BeforeSuite
    public void setup() throws SQLException {

        final Table table = new Table("user");
        final Row sheldon = new Row();

        sheldon.getValues().put("id", "1");
        sheldon.getValues().put("name", "Sheldon");
        sheldon.getValues().put("profession", "physicist");

        table.getRows().add(sheldon);

        final TableDataSetProducer dataSetProducer = new TableDataSetProducer(table);
        final SimpleJdbcController simpleJdbcController = new SimpleJdbcController(dataSetProducer);
        jdbcServer  = new JdbcServer(simpleJdbcController, new JdbcServerConfiguration());

        jdbcServer.startAndAwaitInitialization();
        connection = new JdbcDriver().connect("jdbc:citrus:localhost:4567", new Properties());
    }

    @AfterSuite
    public void tearDown() throws SQLException {
        connection.close();
        jdbcServer.stop();
    }

    @BeforeTest
    public void beforeTest() throws SQLException {
        statement = connection.createStatement();
    }

    @AfterTest
    public void afterTest() throws SQLException {
        statement.close();
    }

    @Test
    public void TestHandleQuery() throws SQLException {

        //GIVEN
        final String query = "SELECT * FROM USER";

        //WHEN
        final ResultSet resultSet = statement.executeQuery(query);

        //THEN
        assertTrue(resultSet.next());
        assertEquals(resultSet.getString("id"), "1");
        assertEquals(resultSet.getString("name"), "Sheldon");
        assertEquals(resultSet.getString("profession"), "physicist");
    }

    @Test
    public void TestHandleUpdate() throws SQLException {

        //GIVEN
        final String query = "UPDATE USER SET 'name' = 'Steven'";

        //WHEN
        final int resultSet = statement.executeUpdate(query);

        //THEN
        assertEquals(resultSet, 0);
    }
}
