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

import com.consol.citrus.db.driver.JdbcDriverException;
import com.consol.citrus.db.driver.data.*;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph Deppisch
 */
public class XmlTableProducer implements TableProducer {

    /** Json data used as table source */
    private final InputStream input;

    public XmlTableProducer(File file) {
        this(file.toPath());
    }

    public XmlTableProducer(Path path) {
        try {
            this.input = new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new JdbcDriverException(e);
        }
    }

    public XmlTableProducer(String jsonInput) {
        this.input = new ByteArrayInputStream(jsonInput.getBytes());
    }

    public XmlTableProducer(InputStream inputStream) {
        this.input = inputStream;
    }

    @Override
    public List<Table> produce() {
        List<Table> tables = new ArrayList<>();

        try {
            DOMImplementationLS domImpl = ((DOMImplementationLS) DOMImplementationRegistry.newInstance().getDOMImplementation("LS"));
            LSParser parser = domImpl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
            LSInput lsInput = domImpl.createLSInput();
            lsInput.setByteStream(input);

            Document document = parser.parse(lsInput);
            NodeList tableNodes = document.getDocumentElement().getChildNodes();

            for (int i = 0; i < tableNodes.getLength(); i++) {
                Node tableNode = tableNodes.item(i);
                if (tableNode instanceof Element) {
                    Table table = new Table(((Element) tableNode).getTagName());
                    NodeList rows = tableNode.getChildNodes();

                    for (int j = 0; j < rows.getLength(); j++) {
                        Node rowNode = rows.item(j);
                        if (rowNode instanceof Element) {
                            Element rowElement = (Element) rowNode;
                            Row row = new Row();

                            NodeList columns = rowElement.getChildNodes();
                            for (int k = 0; k < columns.getLength(); k++) {
                                Node columnNode = columns.item(k);
                                if (columnNode instanceof Element) {
                                    Element columnElement = (Element) columnNode;

                                    StringBuilder nodeValue = new StringBuilder();
                                    NodeList textNodes = columnElement.getChildNodes();
                                    for (int m = 0; m < textNodes.getLength(); m++) {
                                        Node item = textNodes.item(m);
                                        if ((item instanceof CharacterData && !(item instanceof Comment)) || item instanceof EntityReference) {
                                            nodeValue.append(item.getNodeValue());
                                        }
                                    }

                                    row.getValues().put(columnElement.getTagName(), nodeValue.toString());
                                }
                            }

                            for (int k = 0; k < rowElement.getAttributes().getLength(); k++) {
                                Node column = rowElement.getAttributes().item(k);
                                row.getValues().put(column.getLocalName(), column.getNodeValue());
                            }

                            table.getRows().add(row);
                        }
                    }

                    tables.add(table);
                }
            }
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new JdbcDriverException("Unable to read table data set from Json input", e);
        }

        return tables;
    }
}
