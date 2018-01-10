package com.consol.citrus.db.server.rules;

/**
 * @author Christoph Deppisch
 */
public interface Rule<T> {

    boolean matches(T predicate);
}
