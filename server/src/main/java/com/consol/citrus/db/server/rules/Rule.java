package com.consol.citrus.db.server.rules;

import com.consol.citrus.db.server.JdbcServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents a functional predicate (mapping rule) that is applicable on objects of type P, if
 * the objects fulfills a certain boolean-valued precondition predicate. If the rule is applied, the object of type P
 * will be mapped in an object of type R.
 *
 * @author Christoph Deppisch
 */
@SuppressWarnings("unused")
public class Rule<P, R, T extends Rule> {

    /** Logger */
    private Logger log = LoggerFactory.getLogger(getClass());

    /** The predicate that has to be fulfilled so that the rule can be applied */
    private final RuleMatcher<P> ruleMatcher;

    /** The application logic of the rule transforming P -> R */
    private final RuleExecutor<P, R> ruleExecutor;

    /** The amount of invocations */
    private AtomicInteger maxInvocation;

    /** The self reference of this rule */
    private final T self;

    /**
     * Default constructor that accepts all objects and returns null.
     */
    public Rule() {
        this((predicate -> null));
    }

    /**
     * Constructor accepting all objects and a specified functional predicate.
     */
    public Rule(final RuleExecutor<P, R> ruleExecutor) {
        this(RuleMatcher.matchAll(), ruleExecutor);
    }

    /**
     * Constructor accepting a precondition predicate and functional predicate.
     * @param ruleMatcher The predicate that specifies the precondition of the rule
     * @param ruleExecutor The functional predicate to apply to the given object
     */
    public Rule(final RuleMatcher<P> ruleMatcher, final RuleExecutor<P, R> ruleExecutor) {
        this.ruleMatcher = ruleMatcher;
        this.ruleExecutor = ruleExecutor;
        this.self = (T) this;
    }

    /**
     * Determines whether a given object fulfills the precondition predicate of this rule
     * @param candidate The object to evaluate the precondition predicate on
     * @return The result of the predicate evaluation
     */
    public final boolean matches(final P candidate) {
        final boolean matching = (maxInvocation == null || maxInvocation.get() != 0) && ruleMatcher.match(candidate);

        if (matching) {
            log.debug(String.format("Found matching rule for candidate '%s'", Optional.ofNullable(candidate).map(Object::toString).orElse("")));
        } else {
            log.trace(String.format("Rule mismatch for candidate '%s'", Optional.ofNullable(candidate).map(Object::toString).orElse("")));
        }

        return matching;
    }

    /**
     * Applies the rule on the given domain object and returns the mapped codomain object
     * @param domainObject The object to be mapped
     * @return The mapped object of the codomain
     * @throws JdbcServerException In case of an error
     */
    public final R apply(final P domainObject) throws JdbcServerException {
        if (maxInvocation != null) {
            maxInvocation.decrementAndGet();
        }

        return ruleExecutor.then(domainObject);
    }

    /**
     * Specifies the maximum invocations possible for that rule
     * @param times The maximum amount of invocations
     * @return The modified rule
     */
    public T times(final int times) {
        this.maxInvocation = new AtomicInteger(times);
        return self;
    }

    /**
     * Specifies that the rule can be applied without a maximum invocation amount
     * @return The modified rule
     */
    public T anyTimes() {
        this.maxInvocation = null;
        return self;
    }

}
