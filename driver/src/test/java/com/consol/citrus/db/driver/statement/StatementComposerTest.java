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

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class StatementComposerTest {

    private StatementComposer statementComposer = new StatementComposer();

    @Test
    public void testIndexedParametersAreReplacedCorrectly(){

        //GIVEN
        final String statement = "SELECT * FROM table where value=? or citrus=awesome and otherValue=?";
        final String expectedComposedStatement = statement + " - (foobar,foo)";
        final StatementParameters statementParameters = new StatementParameters();
        statementParameters.setParameter(1, "foobar");
        statementParameters.setParameter(2, "foo");

        //WHEN
        final String composedStatement = statementComposer.composeStatement(statement, statementParameters);

        //THEN
        assertEquals(composedStatement, expectedComposedStatement);
    }

    @Test
    public void testNamedParametersAreReplacedCorrectly(){

        //GIVEN
        final String statement = "SELECT * FROM table where value=:value or citrus=awesome and otherValue=:otherValue";
        final String expectedComposedStatement = statement + " - (foobar,foo)";
        final StatementParameters statementParameters = new StatementParameters();
        statementParameters.setParameter("value", "foobar");
        statementParameters.setParameter("otherValue", "foo");

        //WHEN
        final String composedStatement = statementComposer.composeStatement(statement, statementParameters);

        //THEN
        assertEquals(composedStatement, expectedComposedStatement);
    }

    @Test
    public void testMixedParameterTypesAreReplacedCorrectly(){

        //GIVEN
        final String statement = "SELECT * FROM table where value=:value or citrus=? and otherValue=:otherValue or a=?";
        final String expectedComposedStatement = statement + " - (foobar,bar,foo,42)";
        final StatementParameters statementParameters = new StatementParameters();
        statementParameters.setParameter("value", "foobar");
        statementParameters.setParameter(2, "bar");
        statementParameters.setParameter("otherValue", "foo");
        statementParameters.setParameter(4, "42");

        //WHEN
        final String composedStatement = statementComposer.composeStatement(statement, statementParameters);

        //THEN
        assertEquals(composedStatement, expectedComposedStatement);
    }

    @Test
    public void testNoVariableMappingPreservesPlaceholder(){

        //GIVEN
        final String statement = "SELECT * FROM table where value=:value or citrus=? and otherValue=:otherValue or a=?";
        final String expectedComposedStatement = statement + " - (:value,?,:otherValue,?)";

        //WHEN
        final String composedStatement = statementComposer.composeStatement(statement, new StatementParameters());

        //THEN
        assertEquals(composedStatement, expectedComposedStatement);
    }
}