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

package com.consol.citrus.db.driver.dataset;

import com.consol.citrus.db.driver.data.Row;
import com.consol.citrus.db.driver.data.Table;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.TableDataSetProducer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
public class TableDataSetProducerTest {

    private Table table = new Table("user");

    @BeforeClass
    public void setup() {
        Row sheldon = new Row();
        sheldon.getValues().put("id", "1");
        sheldon.getValues().put("name", "Sheldon");
        sheldon.getValues().put("profession", "physicist");

        Row leonard = new Row();
        leonard.getValues().put("id", "2");
        leonard.getValues().put("name", "Leonard");
        leonard.getValues().put("profession", "physicist");
        leonard.getValues().put("email", "leo@bigbangtheory.org");

        Row penny = new Row();
        penny.getValues().put("id", "3");
        penny.getValues().put("name", "Penny");
        penny.getValues().put("profession", "this_and_that");

        table.getRows().add(sheldon);
        table.getRows().add(leonard);
        table.getRows().add(penny);
    }

    @Test
    public void testProduce() throws Exception {
        TableDataSetProducer dataSetProducer = new TableDataSetProducer(table);
        DataSet dataSet = dataSetProducer.produce();
        Assert.assertEquals(dataSet.getColumns().size(), 4);
        Assert.assertEquals(dataSet.getColumns().toString(), "[id, name, profession, email]");
        Assert.assertEquals(dataSet.getRows().size(), 3);
        Assert.assertEquals(dataSet.getRows().get(0).getColumns().toString(), "[id, name, profession]");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("id"), "1");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("name"), "Sheldon");
        Assert.assertEquals(dataSet.getRows().get(1).getColumns().toString(), "[id, name, profession, email]");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("id"), "2");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("name"), "Leonard");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("email"), "leo@bigbangtheory.org");
        Assert.assertEquals(dataSet.getRows().get(2).getColumns().toString(), "[id, name, profession]");
        Assert.assertEquals(dataSet.getRows().get(2).getValue("id"), "3");
        Assert.assertEquals(dataSet.getRows().get(2).getValue("name"), "Penny");
        Assert.assertEquals(dataSet.getRows().get(2).getValue("profession"), "this_and_that");
    }

    @Test
    public void testProduceLimit() throws Exception {
        TableDataSetProducer dataSetProducer = new TableDataSetProducer(table, 2);
        DataSet dataSet = dataSetProducer.produce();
        Assert.assertEquals(dataSet.getColumns().size(), 4);
        Assert.assertEquals(dataSet.getColumns().toString(), "[id, name, profession, email]");
        Assert.assertEquals(dataSet.getRows().size(), 2);
        Assert.assertEquals(dataSet.getRows().get(0).getColumns().toString(), "[id, name, profession]");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("id"), "1");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("name"), "Sheldon");
        Assert.assertEquals(dataSet.getRows().get(1).getColumns().toString(), "[id, name, profession, email]");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("id"), "2");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("name"), "Leonard");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("email"), "leo@bigbangtheory.org");
    }

    @Test
    public void testProduceColumnSelect() throws Exception {
        TableDataSetProducer dataSetProducer = new TableDataSetProducer(table, "id", "name", "email");
        DataSet dataSet = dataSetProducer.produce();
        Assert.assertEquals(dataSet.getColumns().size(), 3);
        Assert.assertEquals(dataSet.getColumns().toString(), "[id, name, email]");
        Assert.assertEquals(dataSet.getRows().size(), 3);
        Assert.assertEquals(dataSet.getRows().get(0).getColumns().toString(), "[id, name, email]");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("id"), "1");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("name"), "Sheldon");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("email"), "");
        Assert.assertEquals(dataSet.getRows().get(1).getColumns().toString(), "[id, name, email]");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("id"), "2");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("name"), "Leonard");
        Assert.assertEquals(dataSet.getRows().get(1).getValue("email"), "leo@bigbangtheory.org");
        Assert.assertEquals(dataSet.getRows().get(2).getColumns().toString(), "[id, name, email]");
        Assert.assertEquals(dataSet.getRows().get(2).getValue("id"), "3");
        Assert.assertEquals(dataSet.getRows().get(2).getValue("name"), "Penny");
        Assert.assertEquals(dataSet.getRows().get(2).getValue("email"), "");
    }

    @Test
    public void testProduceFilter() throws Exception {
        TableDataSetProducer dataSetProducer = new TableDataSetProducer(table).filter("id", "1");
        DataSet dataSet = dataSetProducer.produce();
        Assert.assertEquals(dataSet.getColumns().size(), 3);
        Assert.assertEquals(dataSet.getColumns().toString(), "[id, name, profession]");
        Assert.assertEquals(dataSet.getRows().size(), 1);
        Assert.assertEquals(dataSet.getRows().get(0).getColumns().toString(), "[id, name, profession]");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("id"), "1");
        Assert.assertEquals(dataSet.getRows().get(0).getValue("name"), "Sheldon");

        dataSetProducer = new TableDataSetProducer(table).filter("id", "99");
        dataSet = dataSetProducer.produce();

        Assert.assertEquals(dataSet.getColumns().size(), 0);
        Assert.assertEquals(dataSet.getRows().size(), 0);


        dataSetProducer = new TableDataSetProducer(table).filter("foo", "bar");
        dataSet = dataSetProducer.produce();

        Assert.assertEquals(dataSet.getColumns().size(), 0);
        Assert.assertEquals(dataSet.getRows().size(), 0);
    }

}