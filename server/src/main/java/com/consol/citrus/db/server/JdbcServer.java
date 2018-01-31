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

package com.consol.citrus.db.server;

import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.json.JsonDataSetWriter;
import com.consol.citrus.db.driver.xml.XmlDataSetWriter;
import com.consol.citrus.db.server.controller.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Spark;

import java.util.concurrent.*;

import static spark.Spark.*;

/**
 * @author Christoph Deppisch
 */
public class JdbcServer {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(JdbcServer.class);

    /** Endpoint configuration */
    private final JdbcServerConfiguration configuration;

    /** Controller handling requests */
    private JdbcController controller;

    /**
     * Default constructor initializing controller and configuration.
     */
    public JdbcServer() {
        this(new JdbcServerConfiguration());
    }

    /**
     * Default constructor using a given configuration.
     * @param configuration The configuration of the jdbc server
     */
    public JdbcServer(final JdbcServerConfiguration configuration) {
        this(new SimpleJdbcController(), configuration);
    }

    /**
     * Default constructor using controller and configuration.
     * @param controller The controller to use for request handling
     * @param configuration The configuration of the jdbc server
     */
    public JdbcServer(final JdbcController controller, final JdbcServerConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    public JdbcServer(final String[] args) throws JdbcServerException {
        this();
        new JdbcServerOptions().apply(configuration, args);
    }

    public RuleBasedControllerBuilder when() {
        if (!RuleBasedController.class.isAssignableFrom(controller.getClass())) {
            controller = new RuleBasedController();
        }

        return new RuleBasedControllerBuilder((RuleBasedController) controller);
    }

    /**
     * Main method
     * @param args The command line arguments of the java call
     */
    public static void main(final String[] args) throws JdbcServerException {
        final JdbcServer server = new JdbcServer(args);

        if (server.configuration.getTimeToLive() > 0) {
            CompletableFuture.runAsync(() -> {
                try {
                    new CompletableFuture<Void>().get(server.configuration.getTimeToLive(), TimeUnit.MILLISECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    server.stop();
                }
            });
        }

        server.start();
    }

    /**
     * Start server instance and listen for incoming requests.
     */
    public void start() {
        port(configuration.getPort());

        before((Filter) (request, response) -> log.info(request.requestMethod() + " " + request.url()));

        get("/connection",
                (req, res) -> {
                    controller.openConnection(req.params());
                    return "";
                });

        delete("/connection",
                (req, res) -> {
                    controller.closeConnection();
                    return "";
                });

        get("/connection/transaction",
                (req, res) ->{
                    res.type("application/json");
                    return "{ \"transactionState\": " + controller.getTransactionState() + "}";
                });


        post("/connection/transaction",
                (req, res) -> {
                    controller.setTransactionState(new ObjectMapper().readTree(req.body()).get("transactionState").asBoolean());
                    return "";
                });

        put("/connection/transaction",
                (req, res) -> {
                    controller.commitStatements();
                    return "";
                });

        delete("/connection/transaction",
                (req, res) -> {
                    controller.rollbackStatements();
                    return "";
                });

        get("/statement",
                (req, res) -> {
                    controller.createStatement();
                    return "";
                });

        delete("/statement",
                (req, res) -> {
                    controller.closeStatement();
                    return "";
                });

        post("/statement",
                (req, res) -> {
                    controller.createPreparedStatement(req.body());
                    return "";
                });

        post("/query",
                "application/json",
                (req, res) -> {
                    res.type("application/json");
                    return controller.executeQuery(req.body());
                 },
                model -> new JsonDataSetWriter().write((DataSet) model));

        post("/query",
                "application/xml",
                (req, res) -> {
                    res.type("application/xml");
                    return controller.executeQuery(req.body());
                },
                model -> new XmlDataSetWriter().write((DataSet) model));

        post("/execute",
                (req, res) -> {
                    controller.execute(req.body());
                    return "";
                });

        post("/update",
                (req, res) -> controller.executeUpdate(req.body()));

        exception(JdbcServerException.class, (exception, request, response) -> {
            response.status(500);
            response.body(exception.getMessage());
        });
    }

    /**
     * Stops the server instance.
     */
    public void stop() {
        Spark.stop();
    }
}
