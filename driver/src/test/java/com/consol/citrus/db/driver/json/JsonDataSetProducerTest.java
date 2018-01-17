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

package com.consol.citrus.db.driver.json;

import com.consol.citrus.db.driver.dataset.DataSet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Paths;

/**
 * @author Christoph Deppisch
 */
public class JsonDataSetProducerTest {

    @Test
    public void testProduce() throws Exception {
        JsonDataSetProducer dataSetProducer = new JsonDataSetProducer(Paths.get(ClassLoader.getSystemResource("dataset.json").toURI()));

        DataSet dataSet = dataSetProducer.produce();

        Assert.assertEquals(dataSet.getColumns().size(), 6L);
        Assert.assertEquals(dataSet.getColumns().toString(), "[id, firstname, lastname, username, password, email]");
        Assert.assertEquals(dataSet.getRows().size(), 2L);
        Assert.assertEquals(dataSet.getNextRow().getValues().toString(), "{id=1, firstname=Sheldon, lastname=Cooper, username=flash, password=%$&&%$}");
        Assert.assertEquals(dataSet.getNextRow().getValues().toString(), "{id=2, firstname=Leonard, lastname=Hofstadter, username=batman, password=secr3t, email=leo@bigbangtheory.org}");
    }

}