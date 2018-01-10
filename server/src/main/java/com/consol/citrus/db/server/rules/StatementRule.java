package com.consol.citrus.db.server.rules;

import com.consol.citrus.db.driver.model.ResultSet;

import java.sql.SQLException;

/**
 * @author Christoph Deppisch
 */
public interface StatementRule extends Rule<String> {

    void create() throws SQLException;

    void close() throws SQLException;

    ResultSet executeQuery();

    Integer executeUpdate();
}
