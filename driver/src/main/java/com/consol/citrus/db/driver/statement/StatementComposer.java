package com.consol.citrus.db.driver.statement;

import java.util.stream.Collectors;

class StatementComposer {

    String composeStatement(final String statement, final StatementParameters parameters) {
        return statement + " - (" +
                parameters.getParametersAsMap()
                        .values()
                        .stream()
                        .map(param -> param != null ? param.toString() : "null")
                        .collect(Collectors.joining(",")) + ")";
    }
}
