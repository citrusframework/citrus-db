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
import com.consol.citrus.db.server.rules.Precondition;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ConnectionRuleBuilderTest {

    private final RuleBasedController ruleBasedController = mock(RuleBasedController.class);
    private ConnectionRuleBuilder connectionRuleBuilder = new ConnectionRuleBuilder(ruleBasedController);

    @Test
    public void testClose(){

        //GIVEN

        //WHEN
        final CloseConnectionRuleBuilder closeConnectionRuleBuilder = connectionRuleBuilder.close();

        //THEN
        assertEquals(closeConnectionRuleBuilder.getController(), ruleBasedController);
    }

    @Test
    public void testOpenAcceptsEverything(){

        //GIVEN

        //WHEN
        final OpenConnectionRuleBuilder openConnectionRuleBuilder = connectionRuleBuilder.open();

        //THEN
        assertEquals(openConnectionRuleBuilder.getController(), ruleBasedController);
        assertEquals(openConnectionRuleBuilder.getPrecondition(), Precondition.matchAll());
    }

    @Test
    public void testOpenWithUsername(){

        //GIVEN
        final String username = "Terry Pratchett";
        final Map<String, String> propertiesToMatch = Collections.singletonMap("username", username);

        //WHEN
        final OpenConnectionRuleBuilder openConnectionRuleBuilder = connectionRuleBuilder.open(username);

        //THEN
        assertEquals(openConnectionRuleBuilder.getController(), ruleBasedController);
        assertTrue(openConnectionRuleBuilder.getPrecondition().match(propertiesToMatch));
    }

    @Test
    public void testOpenWithUsernameAndPassword(){

        //GIVEN
        final String username = "TerryPratchett";
        final String password = "ShardsWorld";
        final Map<String, String> propertiesToMatch = new HashMap<>();
        propertiesToMatch.put("username", username);
        propertiesToMatch.put("password", password);

        //WHEN
        final OpenConnectionRuleBuilder openConnectionRuleBuilder = connectionRuleBuilder.open(username, password);

        //THEN
        assertEquals(openConnectionRuleBuilder.getController(), ruleBasedController);
        assertTrue(openConnectionRuleBuilder.getPrecondition().match(propertiesToMatch));
    }

    @Test
    public void testOpenWithRule(){

        //GIVEN
        final String key = "someKey";
        final String value = "somevalue";
        final Map<String, String> propertiesToMatch = Collections.singletonMap(key, value);
        final Precondition<Map<String, String>> matcher = (props) -> props.get(key).equals(value);


        //WHEN
        final OpenConnectionRuleBuilder openConnectionRuleBuilder = connectionRuleBuilder.open(matcher);

        //THEN
        assertEquals(openConnectionRuleBuilder.getController(), ruleBasedController);
        assertTrue(openConnectionRuleBuilder.getPrecondition().match(propertiesToMatch));
    }

    @Test
    public void testStartTransaction(){

        //GIVEN

        //WHEN
        final StartTransactionRuleBuilder startTransactionRuleBuilder = connectionRuleBuilder.startTransaction();

        //THEN
        assertEquals(startTransactionRuleBuilder.getController(), ruleBasedController);
    }

    @Test
    public void testCommitTransaction(){

        //GIVEN

        //WHEN
        final CommitTransactionRuleBuilder commitTransactionRuleBuilder = connectionRuleBuilder.commitTransaction();

        //THEN
        assertEquals(commitTransactionRuleBuilder.getController(), ruleBasedController);
    }

    @Test
    public void testRollbackTransaction(){

        //GIVEN

        //WHEN
        final RollbackTransactionRuleBuilder rollbackTransactionRuleBuilder = connectionRuleBuilder.rollbackTransaction();

        //THEN
        assertEquals(rollbackTransactionRuleBuilder.getController(), ruleBasedController);
    }
}