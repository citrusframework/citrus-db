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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Christoph Deppisch
 * @since 2.7
 */
public class XmlDataSetWriterTest {
    
    @Test
    public void testWrite() throws Exception {
        Assert.assertEquals(
                new XmlDataSetWriter().write(new XmlDataSetProducer(Paths.get(ClassLoader.getSystemResource("dataset.xml").toURI())).produce()),
                new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("dataset.xml").toURI()))));
    }

    @Test
    public void testWriteAttributes() throws Exception {
        XmlDataSetWriter writer = new XmlDataSetWriter();
        writer.setMode(Node.ATTRIBUTE_NODE);

        Assert.assertEquals(
                writer.write(new XmlDataSetProducer(Paths.get(ClassLoader.getSystemResource("dataset_attribute.xml").toURI())).produce()),
                "<dataset>\n" +
                        "  <row\n" +
                        "    firstname=\"Sheldon\"\n" +
                        "    id=\"1\"\n" +
                        "    lastname=\"Cooper\"\n" +
                        "    password=\"gordon\"\n" +
                        "    username=\"flash\"\n" +
                        "    />\n" +
                        "  <row\n" +
                        "    email=\"leo@bigbangtheory.org\"\n" +
                        "    firstname=\"Leonard\"\n" +
                        "    id=\"2\"\n" +
                        "    lastname=\"Hofstadter\"\n" +
                        "    password=\"wayne\"\n" +
                        "    username=\"batman\"\n" +
                        "    />\n" +
                        "</dataset>");
    }

}