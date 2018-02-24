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

package com.consol.citrus.db.server.transformer;

import com.consol.citrus.db.driver.dataset.DataSet;
import com.consol.citrus.db.driver.json.JsonDataSetWriter;
import spark.ResponseTransformer;

import java.sql.SQLException;

public class JsonResponseTransformer implements ResponseTransformer {

    private JsonDataSetWriter jsonDataSetWriter = new JsonDataSetWriter();

    public JsonResponseTransformer() {
    }

    JsonResponseTransformer(final JsonDataSetWriter jsonDataSetWriter) {
        this.jsonDataSetWriter = jsonDataSetWriter;
    }

    @Override
    public String render(final Object model) throws SQLException {
        return jsonDataSetWriter.write((DataSet) model);
    }
}
