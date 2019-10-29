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


import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class StatementParametersTest {

    private StatementParameters statementParameters;

    @BeforeMethod
    public void setup(){
        statementParameters = new StatementParameters();
    }

    @Test
    public void testCopyConstructorForIndexedParametersAndNamedParameters(){

        //GIVEN
        final String firstParameter = "first";
        final String secondParameter = "second";
        statementParameters.setParameter(1, firstParameter);
        statementParameters.setParameter("secondParameter", secondParameter);

        //WHEN
        final StatementParameters copy = new StatementParameters(statementParameters);

        //THEN
        assertEquals(copy, statementParameters);
    }

    @Test
    public void testGetIndexedParameters(){

        //GIVEN
        final String firstParameter = "first";
        final String secondParameter = "second";
        statementParameters.setParameter(1, firstParameter);
        statementParameters.setParameter(2, secondParameter);

        //WHEN
        final Object gottenFirstParameter = statementParameters.get(1);
        final Object gottenSecondParameter = statementParameters.get(2);

        //THEN
        assertEquals(gottenFirstParameter, firstParameter);
        assertEquals(gottenSecondParameter, secondParameter);
    }

    @Test
    public void testGetNamedParameters(){

        //GIVEN
        final String firstParameter = "first";
        final String secondParameter = "second";
        statementParameters.setParameter("firstParameter", firstParameter);
        statementParameters.setParameter("secondParameter", secondParameter);

        //WHEN
        final Object gottenFirstParameter = statementParameters.get("firstParameter");
        final Object gottenSecondParameter = statementParameters.get("secondParameter");

        //THEN
        assertEquals(gottenFirstParameter, firstParameter);
        assertEquals(gottenSecondParameter, secondParameter);
    }

    @Test
    public void testSize(){

        //GIVEN
        final String firstParameter = "first";
        final String secondParameter = "second";
        statementParameters.setParameter(1, firstParameter);
        statementParameters.setParameter("secondParameter", secondParameter);

        //WHEN
        final int size = statementParameters.size();

        //THEN
        assertEquals(size, 2);
    }

    @Test
    public void testClear(){

        //GIVEN
        final String firstParameter = "first";
        final String secondParameter = "second";
        statementParameters.setParameter(1, firstParameter);
        statementParameters.setParameter("secondParameter", secondParameter);

        //WHEN
        statementParameters.clear();

        //THEN
        assertEquals(statementParameters.size(), 0);
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(StatementParameters.class)
                .verify();
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(StatementParameters.class)
                .verify();
    }
}