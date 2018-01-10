package com.consol.citrus.db.server.rules;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Christoph Deppisch
 */
public interface ConnectionRule extends Rule<Map<String, String>> {

    void open() throws SQLException;

    void close() throws SQLException;
}
