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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StatementParameters {

    private final Map<String, Object> namedParameters = new HashMap<>();
    private final Map<Integer, Object> indexedParameters = new HashMap<>();

    StatementParameters() {
    }

    StatementParameters(final StatementParameters statementParameters) {
        namedParameters.putAll(statementParameters.namedParameters);
        indexedParameters.putAll(
                importParameterList(new ArrayList<>(statementParameters.indexedParameters.values())));
    }

    public Object get(final int parameterIndex) {
        return indexedParameters.get(parameterIndex-1);
    }

    public Object get(final String namedParameter) {
        return namedParameters.get(namedParameter);
    }

    public int size() {
        return namedParameters.size() + indexedParameters.size();
    }

    void setParameter(final String parameterName, final Object value) {
        namedParameters.put(parameterName, value);
    }

    void setParameter(final int parameterIndex, final Object value) {
        indexedParameters.put(parameterIndex-1, value);
    }

    void clear() {
        namedParameters.clear();
        indexedParameters.clear();
    }

    private Map<Integer, Object> importParameterList(final List<Object> statementParameters) {
        final HashMap<Integer, Object> parameterMap = new HashMap<>();
        for (int i = 0; i < statementParameters.size(); i++) {
            parameterMap.put(i, statementParameters.get(i));
        }
        return parameterMap;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof StatementParameters)) return false;
        final StatementParameters that = (StatementParameters) o;
        return Objects.equals(namedParameters, that.namedParameters) &&
                Objects.equals(indexedParameters, that.indexedParameters);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(namedParameters, indexedParameters);
    }

    @Override
    public String toString() {
        return "StatementParameters{" +
                "namedParameters=" + namedParameters +
                ", indexedParameters=" + indexedParameters +
                '}';
    }
}
