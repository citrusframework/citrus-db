package com.consol.citrus.db.driver.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class StatementParameters {

    private Map<String, Object> namedParameters = new HashMap<>();
    private Map<Integer, Object> indexedParameters = new HashMap<>();

    StatementParameters(final StatementParameters statementParameters) {
        this.namedParameters = new HashMap<>(statementParameters.getParametersAsMap());

        final List<Object> originalIndexParameters = statementParameters.getParametersAsList();
        this.indexedParameters = IntStream
                .range(0, originalIndexParameters.size())
                .boxed()
                .collect(toMap(Function.identity(), originalIndexParameters::get));
    }

    StatementParameters() {
    }

    public Object get(final int index) {
        return indexedParameters.get(index);
    }

    public Object get(final String namedParameter) {
        return namedParameters.get(namedParameter);
    }

    void setParameter(final String parameterName, final Object value) {
        namedParameters.put(parameterName, value);
    }

    void setParameter(final int parameterIndex, final Object value) {
        indexedParameters.put(parameterIndex-1, value);
    }

    Map<String, Object> getParametersAsMap(){
        return namedParameters;
    }

    List<Object> getParametersAsList() {
        return new ArrayList<>(indexedParameters.values());
    }

    void clear() {
        namedParameters.clear();
        indexedParameters.clear();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof StatementParameters)) return false;
        final StatementParameters that = (StatementParameters) o;
        return Objects.equals(namedParameters, that.namedParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namedParameters);
    }

    @Override
    public String toString() {
        return "StatementParameters{" +
                "parameters=" + namedParameters +
                '}';
    }

    public int size() {
        return namedParameters.size() + indexedParameters.size();
    }
}
