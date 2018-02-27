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
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.DataSetWriter;
import org.w3c.dom.Node;

import java.sql.SQLException;

/**
 * @author Christoph Deppisch
 */
public class XmlDataSetWriter implements DataSetWriter {

    private final String DATASET = "dataset";
    private final String ROW = "row";

    private short mode = Node.ELEMENT_NODE;
    private String spaces = "  ";

    @Override
    public String write(DataSet dataSet) {
        try {
            StringBuilder xmlOutput = new StringBuilder();

            xmlOutput.append("<").append(DATASET).append(">\n");

            dataSet.getRows().forEach(row -> {
                if (mode == Node.ELEMENT_NODE) {
                    xmlOutput.append(spaces).append("<").append(ROW).append(">\n");
                    row.getValues().forEach((key, value) -> {
                        xmlOutput.append(spaces).append(spaces).append("<").append(key).append(">");
                        xmlOutput.append(value);
                        xmlOutput.append("</").append(key).append(">\n");
                    });
                    xmlOutput.append(spaces).append("</").append(ROW).append(">\n");
                } else if (mode == Node.ATTRIBUTE_NODE) {
                    xmlOutput.append(spaces).append("<").append(ROW).append("\n");
                    row.getValues().forEach((key, value) -> {
                        xmlOutput.append(spaces).append(spaces).append(key).append("=\"").append(value).append("\"").append("\n");
                    });
                    xmlOutput.append(spaces).append(spaces).append("/>\n");
                }
            });

            xmlOutput.append("</").append(DATASET).append(">");

            return xmlOutput.toString();
        } catch (SQLException e) {
            throw new JdbcDriverException("Failed to write xml dataset", e);
        }
    }

    /**
     * Gets the mode.
     *
     * @return
     */
    public short getMode() {
        return mode;
    }

    /**
     * Sets the mode.
     *
     * @param mode
     */
    public void setMode(short mode) {
        this.mode = mode;
    }

    /**
     * Gets the indent.
     *
     * @return
     */
    public int getIndent() {
        return spaces.length();
    }

    /**
     * Sets the indent.
     *
     * @param indent
     */
    public void setIndent(int indent) {
        StringBuilder spacesBuilder = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            spacesBuilder.append(" ");
        }

        this.spaces = spacesBuilder.toString();
    }
}
