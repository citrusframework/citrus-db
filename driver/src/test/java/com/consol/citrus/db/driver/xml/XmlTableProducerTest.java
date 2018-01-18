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

package com.consol.citrus.db.driver.xml;

import com.consol.citrus.db.driver.data.Table;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.List;

/**
 * @author Christoph Deppisch
 */
public class XmlTableProducerTest {

    @Test
    public void testProduce() throws Exception {
        XmlTableProducer dataSetProducer = new XmlTableProducer(Paths.get(ClassLoader.getSystemResource("database.xml").toURI()));

        List<Table> tables = dataSetProducer.produce();

        Assert.assertEquals(tables.size(), 2L);

        Assert.assertEquals(tables.get(0).getName(), "characters");
        Assert.assertEquals(tables.get(0).getColumns().toString(), "[id, firstname, lastname, username, password, email]");
        Assert.assertEquals(tables.get(0).getRows().size(), 2L);
        Assert.assertEquals(tables.get(0).getRows().get(0).getValues().toString(), "{id=1, firstname=Sheldon, lastname=Cooper, username=flash, password=gordon}");
        Assert.assertEquals(tables.get(0).getRows().get(1).getValues().toString(), "{id=2, firstname=Leonard, lastname=Hofstadter, username=batman, password=wayne, email=leo@bigbangtheory.org}");

        Assert.assertEquals(tables.get(1).getName(), "friends");
        Assert.assertEquals(tables.get(1).getColumns().toString(), "[id, firstname, lastname, email]");
        Assert.assertEquals(tables.get(1).getRows().size(), 2L);
        Assert.assertEquals(tables.get(1).getRows().get(0).getValues().toString(), "{id=3, firstname=Rajesh, lastname=Koothrappali}");
        Assert.assertEquals(tables.get(1).getRows().get(1).getValues().toString(), "{id=4, firstname=Howard, lastname=Wolowitz, email=womanizr@bigbangtheory.org}");
    }

}