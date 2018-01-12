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

package com.consol.citrus.db.driver;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Christoph Deppisch
 */
public class JdbcDriverTest {

    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private JdbcDriver driver;

    @BeforeClass
    public void setup() {
        driver = new JdbcDriver(httpClient);
    }

    @Test(dataProvider = "urlProvider")
    public void connect(String url, int port, String dbName) throws SQLException, IOException {
        when(httpClient.execute(any(HttpUriRequest.class))).thenAnswer((Answer<HttpResponse>) invocation -> {
                HttpUriRequest request = invocation.getArgument(0);
                Assert.assertEquals(request.getURI().toString(), "http://localhost:" + port + "/connection?database=" + dbName);
                return new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
            }
        );

        driver.connect(url, new Properties());
    }

    @DataProvider
    private static Object[][] urlProvider() {
        return new Object[][] { new Object[] { "jdbc:db2://localhost:1234/testdb", 1234, "testdb" },
                                new Object[] { "jdbc:odbc:testdb", 4567, "testdb" },
                                new Object[] { "jdbc:oracle:thin:@localhost:1234:testdb", 1234, "testdb" },
                                new Object[] { "jdbc:pointbase://localhost:1234/testdb", 1234, "testdb" },
                                new Object[] { "jdbc:cloudscape:testdb", 4567, "testdb" },
                                new Object[] { "jdbc:rmi://localhost:1234/jdbc:cloudscape:testdb", 1234, "testdb" },
                                new Object[] { "jdbc:firebirdsql://localhost:1234/testdb", 1234, "testdb" },
                                new Object[] { "jdbc:informix-sqli://localhost:1234/testdb:INFORMIXSERVER=localhost", 1234, "testdb%3AINFORMIXSERVER%3Dlocalhost" },
                                new Object[] { "jdbc:idb:testdb", 4567, "testdb" },
                                new Object[] { "jdbc:interbase://localhost/testdb", 4567, "testdb" },
                                new Object[] { "jdbc:HypersonicSQL:testdb", 4567, "testdb" },
                                new Object[] { "jdbc:JTurbo://localhost:1234/testdb", 1234, "testdb" },
                                new Object[] { "jdbc:inetdae:localhost:1234?database=testdb", 1234, "testdb" },
                                new Object[] { "jdbc:microsoft:sqlserver://localhost:1234", 1234, "" },
                                new Object[] { "jdbc:microsoft:sqlserver://localhost:1234;DatabaseName=testdb", 1234, "testdb" },
                                new Object[] { "jdbc:weblogic:mssqlserver4:testdb@localhost:1234", 1234, "testdb" },
                                new Object[] { "jdbc:mysql://localhost:1234/testdb", 1234, "testdb" },
                                new Object[] { "jdbc:oracle:oci8:@testdb", 4567, "testdb" },
                                new Object[] { "jdbc:oracle:oci:@testdb", 4567, "testdb" },
                                new Object[] { "jdbc:postgresql://localhost:1234/testdb", 1234, "testdb" },
                                new Object[] { "jdbc:sybase:Tds:localhost:1234", 1234, "" }
        };
    }

}