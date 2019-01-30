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

package com.consol.citrus.demo;

import com.consol.citrus.annotations.CitrusEndpoint;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.config.annotation.HttpClientConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
public class DemoApplicationIT extends TestNGCitrusTestRunner {

    @CitrusEndpoint
    @HttpClientConfig(requestUrl = "http://localhost:8080")
    private HttpClient httpClient;

    @Test
    @CitrusTest
    public void testUser() {
        http(action -> action.client(httpClient)
                        .send()
                        .get("/user/add")
                        .queryParam("name", "Sheldon")
                        .queryParam("email", "sheldon@bbtheory.org"));

        http(action -> action.client(httpClient)
                        .receive()
                        .response(HttpStatus.OK)
                        .payload("Saved"));

        http(action -> action.client(httpClient)
                .send()
                .get("/user/all"));

        http(action -> action.client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .payload(new ClassPathResource("users.json")));
    }

    @Test
    @CitrusTest
    public void testCity() {
        http(action -> action.client(httpClient)
                .send()
                .get("/city/add")
                .queryParam("names", "San+Francisco,Regensburg"));

        http(action -> action.client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .payload("Saved"));

        http(action -> action.client(httpClient)
                .send()
                .get("/city/all"));

        http(action -> action.client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .payload(new ClassPathResource("cities.json")));
    }

    @Test
    @CitrusTest
    public void testGetIdByCityName() {
        http(action -> action.client(httpClient)
                .send()
                .get("/city/findId")
                .queryParam("name", "Munich"));

        http(action -> action.client(httpClient)
                .receive()
                .response(HttpStatus.OK)
                .payload("{ \"name\": \"Munich\", \"id\": 1 }"));
    }
}