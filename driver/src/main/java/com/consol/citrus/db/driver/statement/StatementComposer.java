package com.consol.citrus.db.driver.statement;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class StatementComposer {

    String composeStatement(final String statement, final StatementParameters parameters) {
        return statement + " - (" +
                determineParameterOrder(statement, parameters)
                        .stream()
                        .map(param -> param != null ? param.toString() : "null")
                        .collect(Collectors.joining(",")) + ")";
    }

    private Collection<Object> determineParameterOrder(final String statement,
                                                       final StatementParameters parameters) {
        final Pattern parameterPattern = Pattern.compile("(:[a-zA-Z]+|\\?)");
        final Matcher parameterMatcher = parameterPattern.matcher(statement);

        final LinkedList<Object> orderedParameterList = new LinkedList<>();

        for(int matchIndex = 1; parameterMatcher.find(); matchIndex++){
            final String parameter = parameterMatcher.group(1);
            if(parameter != null) {
                if(parameter.startsWith(":")){
                    final String parameterName = parameter.replace(":", "");
                    orderedParameterList.add(parameters.get(parameterName));
                }else
                    orderedParameterList.add(parameters.get(matchIndex));
            }
        }

        return orderedParameterList;
    }
}
