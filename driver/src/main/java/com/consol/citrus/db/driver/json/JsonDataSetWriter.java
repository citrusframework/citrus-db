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

import com.consol.citrus.db.driver.JdbcDriverException;
import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.dataset.DataSetWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Christoph Deppisch
 */
public class JsonDataSetWriter implements DataSetWriter {

    @Override
    public String write(final DataSet dataSet) {
        try {
            final List<Map<String, Object>> rawDataSet = new ArrayList<>();
            dataSet.getRows().forEach(row -> rawDataSet.add(row.getValues()));

            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(rawDataSet);
        } catch (final JsonProcessingException e) {
            throw new JdbcDriverException("Failed to write json dataset", e);
        }
    }
}
