package com.consol.citrus.db.server.rules;

import com.consol.citrus.db.server.JdbcServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Christoph Deppisch
 */
public class Rule<P, R, T extends Rule> {

    /** Logger */
    private Logger log = LoggerFactory.getLogger(getClass());

    private final RuleMatcher<P> ruleMatcher;
    private final RuleExecutor<P, R> ruleExecutor;

    private AtomicInteger maxInvocation;

    private final T self;

    /**
     * Default constructor using all matcher and null executor.
     */
    public Rule() {
        this((predicate -> null));
    }

    /**
     * Constructor using all matcher and executor.
     */
    public Rule(RuleExecutor<P, R> ruleExecutor) {
        this(RuleMatcher.matchAll(), ruleExecutor);
    }

    /**
     * Constructor using matcher and executor.
     * @param ruleMatcher
     * @param ruleExecutor
     */
    public Rule(RuleMatcher<P> ruleMatcher, RuleExecutor<P, R> ruleExecutor) {
        this.ruleMatcher = ruleMatcher;
        this.ruleExecutor = ruleExecutor;
        this.self = (T) this;
    }

    public final boolean matches(P predicate) {
        boolean matching = (maxInvocation == null || maxInvocation.get() != 0) && ruleMatcher.match(predicate);

        if (matching) {
            log.debug(String.format("Found matching rule for predicate '%s'", Optional.ofNullable(predicate).map(Object::toString).orElse("")));
        } else {
            log.trace(String.format("Rule mismatch for predicate '%s'", Optional.ofNullable(predicate).map(Object::toString).orElse("")));
        }

        return matching;
    }

    public final R apply(P predicate) throws JdbcServerException {
        if (maxInvocation != null) {
            maxInvocation.decrementAndGet();
        }

        return ruleExecutor.then(predicate);
    }

    public T times(int times) {
        this.maxInvocation = new AtomicInteger(times);
        return self;
    }

    public T anyTimes() {
        this.maxInvocation = null;
        return self;
    }

}
