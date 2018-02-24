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

package com.consol.citrus.db.server;

/**
 * @author Christoph Deppisch
 */
public class JdbcServerConfiguration {

    /** Server connection parameters */
    private String host;
    private int port = 4567;

    /** Default database name */
    private String databaseName;

    /** Maximum number of parallel connections */
    private int maxConnections = 20;

    /** Server time to live in milliseconds */
    private long timeToLive = -1;

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * Gets the databaseName.
     *
     * @return The name of the database
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Sets the databaseName.
     *
     * @param databaseName The database name to set
     */
    public void setDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Gets the maxConnections.
     *
     * @return the amount of max connections
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * Sets the maxConnections.
     *
     * @param maxConnections The amount of max connections
     */
    public void setMaxConnections(final int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     * Gets the timeToLive.
     *
     * @return The time to live for the server
     */
    public long getTimeToLive() {
        return timeToLive;
    }

    /**
     * Sets the timeToLive.
     *
     * @param timeToLive The time to live for the server
     */
    public void setTimeToLive(final long timeToLive) {
        this.timeToLive = timeToLive;
    }
}
