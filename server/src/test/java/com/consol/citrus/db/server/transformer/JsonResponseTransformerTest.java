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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JsonResponseTransformerTest {

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final JsonResponseTransformer jsonResponseTransformer = new JsonResponseTransformer(objectMapper);

    @Test
    public void testResponseTransformation() throws Exception {

        //GIVEN
        final DataSet dataSet = mock(DataSet.class);
        final String expectedRenderedResponse = String.valueOf(UUID.randomUUID());
        when(objectMapper.writeValueAsString(dataSet)).thenReturn(expectedRenderedResponse);

        //WHEN
        final String renderedResponse = jsonResponseTransformer.render(dataSet);

        //THEN
        Assert.assertEquals(renderedResponse, expectedRenderedResponse);
        verify(objectMapper).writeValueAsString(dataSet);
    }
}