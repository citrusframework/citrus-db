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

import com.consol.citrus.db.driver.data.Table;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.TableDataSetProducer;
import com.consol.citrus.db.driver.json.JsonDataSetProducer;
import com.consol.citrus.db.driver.xml.XmlDataSetProducer;
import com.consol.citrus.db.server.controller.RuleBasedController;
import com.consol.citrus.db.server.rules.ExecuteQueryRule;
import com.consol.citrus.db.server.rules.Precondition;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class ExecuteQueryRuleBuilderTest {

    private Precondition<String> precondition = Precondition.matchAll();
    private RuleBasedController ruleBasedControllerMock = mock(RuleBasedController.class);
    private ExecuteQueryRuleBuilder executeQueryRuleBuilder =
            new ExecuteQueryRuleBuilder(precondition, ruleBasedControllerMock);

    @Test
    public void testThenReturnDataSet(){

        //GIVEN
        final DataSet dataSet = mock(DataSet.class);

        //WHEN
        final ExecuteQueryRule rule = executeQueryRuleBuilder.thenReturn(dataSet);

        //THEN
        verify(ruleBasedControllerMock).add(rule);
        assertEquals(rule.applyOn("whatever"), dataSet);
    }

    @Test
    public void testThenReturnFile() throws Exception{

        //GIVEN
        final ExecuteQueryRuleBuilder executeQueryRuleBuilder =
                spy(new ExecuteQueryRuleBuilder(precondition, ruleBasedControllerMock));
        final File file = new File(ClassLoader.getSystemResource("dataset.json").toURI().getPath());
        final DataSet expectedDataSet = new JsonDataSetProducer(file).produce();

        //WHEN
        final ExecuteQueryRule rule = executeQueryRuleBuilder.thenReturn(file);

        //THEN
        verify(ruleBasedControllerMock).add(rule);
        verify(executeQueryRuleBuilder).thenReturn(file.toPath());
        assertEquals(rule.applyOn("whatever"), expectedDataSet);
    }

    @Test
    public void testThenReturnPathWithJson() throws Exception {

        //GIVEN
        final Path path = Paths.get(ClassLoader.getSystemResource("dataset.json").toURI());
        final DataSet expectedDataSet = new JsonDataSetProducer(path).produce();

        //WHEN
        final ExecuteQueryRule rule = executeQueryRuleBuilder.thenReturn(path);

        //THEN
        verify(ruleBasedControllerMock).add(rule);
        assertEquals(rule.applyOn("whatever"), expectedDataSet);
    }

    @Test
    public void testThenReturnPathWithXml() throws Exception {

        //GIVEN
        final Path path = Paths.get(ClassLoader.getSystemResource("dataset.xml").toURI());
        final DataSet expectedDataSet = new XmlDataSetProducer(path).produce();

        //WHEN
        final ExecuteQueryRule rule = executeQueryRuleBuilder.thenReturn(path);

        //THEN
        verify(ruleBasedControllerMock).add(rule);
        assertEquals(rule.applyOn("whatever"), expectedDataSet);
    }

    @Test
    public void testThenReturnPathWithUnknown() throws Exception {

        //GIVEN
        final Path path = Paths.get("some useless path");
        final DataSet expectedDataSet = new TableDataSetProducer(new Table("empty")).produce();

        //WHEN
        final ExecuteQueryRule rule = executeQueryRuleBuilder.thenReturn(path);

        //THEN
        verify(ruleBasedControllerMock).add(rule);
        assertEquals(rule.applyOn("whatever"), expectedDataSet);
    }
}