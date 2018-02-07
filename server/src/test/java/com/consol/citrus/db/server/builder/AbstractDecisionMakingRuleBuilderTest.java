/*
 * Copyright 2006-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.db.server.builder;

import com.consol.citrus.db.server.controller.RuleBasedController;
import com.consol.citrus.db.server.rules.Mapping;
import com.consol.citrus.db.server.rules.OpenConnectionRule;
import com.consol.citrus.db.server.rules.Precondition;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class AbstractDecisionMakingRuleBuilderTest {

    private RuleBasedController controllerMock = mock(RuleBasedController.class);
    private OpenConnectionRule openConnectionRule;

    //Class under test
    private AbstractDecisionMakingRuleBuilder<OpenConnectionRule, Map<String,String>> builder;

    @BeforeTest
    public void setUp(){
        builder = spy(new AbstractDecisionMakingRuleBuilder<OpenConnectionRule, Map<String,String>>(controllerMock) {
            @Override
            protected OpenConnectionRule createRule(
                    final Precondition<Map<String, String>> precondition,
                    final Mapping<Map<String, String>, Boolean> mapping) {
                openConnectionRule = new OpenConnectionRule(precondition, mapping);
                return openConnectionRule;
            }
        });

    }

    @Test
    public void testThenAcceptWithPrecondition(){

        //GIVEN
        final Precondition<Map<String,String>> preconditionMock =
                (Precondition<Map<String,String>>) mock(Precondition.class);
        when(preconditionMock.match(anyMap())).thenReturn(true);

        builder.setPrecondition(preconditionMock);

        //WHEN
        final OpenConnectionRule openConnectionRule = builder.thenAccept();

        //THEN
        verify(builder).createRule(eq(preconditionMock), any());
        verify(controllerMock).add(openConnectionRule);
        assertTrue(openConnectionRule.applyOn(new HashMap<>()));
    }

    @Test
    public void testThenAcceptWithoutPrecondition(){

        //GIVEN

        //WHEN
        final OpenConnectionRule openConnectionRule = builder.thenAccept();

        //THEN
        verify(builder).createRule(eq(Precondition.matchAll()), any());
        verify(controllerMock).add(openConnectionRule);
        assertTrue(openConnectionRule.applyOn(new HashMap<>()));
    }

    @Test
    public void testThenRefuseWithPrecondition(){

        //GIVEN
        final Precondition<Map<String,String>> preconditionMock =
                (Precondition<Map<String,String>>) mock(Precondition.class);
        when(preconditionMock.match(anyMap())).thenReturn(true);

        builder.setPrecondition(preconditionMock);

        //WHEN
        final OpenConnectionRule openConnectionRule = builder.thenRefuse();

        //THEN
        verify(builder).createRule(eq(preconditionMock), any());
        verify(controllerMock).add(openConnectionRule);
        assertFalse(openConnectionRule.applyOn(new HashMap<>()));
    }

    @Test
    public void testThenRefuseWithoutPrecondition(){

        //GIVEN

        //WHEN
        final OpenConnectionRule openConnectionRule = builder.thenRefuse();

        //THEN
        verify(builder).createRule(eq(Precondition.matchAll()), any());
        verify(controllerMock).add(openConnectionRule);
        assertFalse(openConnectionRule.applyOn(new HashMap<>()));
    }
}