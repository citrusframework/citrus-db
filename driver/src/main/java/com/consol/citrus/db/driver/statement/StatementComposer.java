/*
 *  Copyright 2006-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
            final String parameterPlaceholder = parameterMatcher.group(1);
            if(parameterPlaceholder != null) {
                orderedParameterList.add(
                        getParameterValue(parameters, parameterPlaceholder, matchIndex));
            }
        }
        return orderedParameterList;
    }

    private Object getParameterValue(final StatementParameters parameters,
                                     final String parameterPlaceholder,
                                     final int matchIndex) {
        final Object parameterValue;
        if(parameterPlaceholder.startsWith(":")){
            final String parameterName = parameterPlaceholder.replace(":", "");
            parameterValue = parameters.get(parameterName);
        }else{
            parameterValue = parameters.get(matchIndex);
        }

        if(parameterValue != null){
            return parameterValue;
        }else{
            return parameterPlaceholder;
        }
    }
}
