/*
 * Copyright 2006-2017 the original author or authors.
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

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Christoph Deppisch
 */
public class JdbcDriver implements Driver {

    /** Client connects to db server */
    private final HttpClient httpClient;

    /** Remote server url */
    private String serverUrl;

    /** Connection timeout */
    private static int timeout = 5000;

    /** Driver URL prefix */
    private static final String[] URL_PREFIX_SET = { "jdbc:citrus:",
            "jdbc:odbc:",
            "jdbc:hsql",
            "jdbc:weblogic:",
            "jdbc:microsoft:",
            "jdbc:oracle:oci:",
            "jdbc:oracle:oci8:",
            "jdbc:oracle:thin:",
            "jdbc:sybase:Tds:",
            "jdbc:inetdae:",
            "jdbc:" };

    public static final int MAJOR = 0;
    public static final int MINOR = 1;
    public static final int PATCH = 0;

    public static final JdbcDriver driverInstance = new JdbcDriver();

    /**
     * Default constructor creates default http client.
     */
    public JdbcDriver() {
        this(HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT)
                        .setConnectionRequestTimeout(timeout)
                        .setConnectTimeout(timeout)
                        .setSocketTimeout(timeout)
                        .build())
                .build());
    }

    /**
     * Constructor using http client.
     * @param httpClient
     */
    public JdbcDriver(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    static {
        try {
            DriverManager.registerDriver(driverInstance);
        } catch(Exception e) {
            LoggerFactory.getLogger(JdbcDriver.class).warn("Error registering jdbc driver", e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        JdbcConnection connection = null;

        if (acceptsURL(url)) {
            try {
                connectRemote(url, info);
                connection = new JdbcConnection(httpClient, serverUrl);
            } catch(Exception ex) {
                throw(new SQLException(ex.getMessage(), ex));
            }
        }

        return connection;
    }

    /**
     * This method makes the one time connection to the RMI server
     * @param connectionString
     * @param info
     */
    private void connectRemote(String connectionString, Properties info) throws SQLException {
        HttpResponse response = null;
        try {
            URI uri = new URI(getServerUri(connectionString));
            serverUrl = "http://" + Optional.ofNullable(uri.getHost()).orElse("localhost") + (uri.getPort() > 0 ? ":" + uri.getPort() : ":4567");

            response = httpClient.execute(RequestBuilder.get(serverUrl + "/connection")
                    .addParameter("database", getDatabaseName(uri))
                    .addParameters(info.entrySet()
                                        .stream()
                                        .map(entry -> new BasicNameValuePair(entry.getKey().toString(), entry.getValue() != null ? entry.getValue().toString() : ""))
                                        .collect(Collectors.toList()).toArray(new NameValuePair[info.size()]))
                    .build());

            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                throw new SQLException("Failed to connect to server: " + EntityUtils.toString(response.getEntity()));
            }
        } catch(Exception ex) {
            throw new SQLException(ex);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return Stream.of(URL_PREFIX_SET).anyMatch(url::startsWith);
    }

    /**
     * Extracts database name from connection string uri.
     * @param uri
     * @return
     */
    private String getDatabaseName(URI uri) {
        if (uri.getSchemeSpecificPart().contains("?database=")) {
            return uri.getSchemeSpecificPart().substring(uri.getSchemeSpecificPart().indexOf("?database=") + "?database=".length());
        } else if (uri.getSchemeSpecificPart().contains(";DatabaseName=")) {
            return uri.getSchemeSpecificPart().substring(uri.getSchemeSpecificPart().indexOf(";DatabaseName=") + ";DatabaseName=".length());
        } else if (uri.getSchemeSpecificPart().contains("/")) {
            return uri.getSchemeSpecificPart().substring(uri.getSchemeSpecificPart().lastIndexOf('/') + 1);
        } else {
            return uri.getSchemeSpecificPart();
        }
    }

    /**
     * Constructs proper db connection url from JDBC connection string.
     * @param connectionString
     * @return
     */
    private String getServerUri(String connectionString) {
        String url = connectionString.substring(Stream.of(URL_PREFIX_SET).filter(connectionString::startsWith).findFirst().orElse("").length());
        if (url.startsWith("@")) {
            String[] tokens = url.substring(1).split(":");

            if (tokens.length > 2) {
                url = tokens[0] + ":" + tokens[1] + "/" + tokens[2];
            } else if (tokens.length > 1) {
                url = tokens[0] + "/" + tokens[1];
            } else {
                url = "localhost/" + tokens[0];
            }
        }

        if (url.startsWith("mssqlserver4:")) {
            String[] tokens = url.substring("mssqlserver4:".length()).split("@");
            url = tokens[1] + "/" + tokens[0];
        }

        return url;
    }

    @Override
    public int getMajorVersion() {
        return MAJOR;
    }

    @Override
    public int getMinorVersion() {
        return MINOR;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties loginProps) throws SQLException {
        return new DriverPropertyInfo[] {};
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getGlobal();
    }
}