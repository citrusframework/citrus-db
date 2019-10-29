/*
 *  Copyright 2006-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.consol.citrus.demo;

import com.consol.citrus.db.driver.json.JsonDataSetProducer;
import com.consol.citrus.db.server.JdbcServer;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * @author Christoph Deppisch
 */
public class DemoJdbcServer {

    public static void main(final String[] args) throws IOException, SQLException {
        final JdbcServer dbServer = new JdbcServer(args);

        dbServer.when()
                .statement()
                .executeQuery(Pattern.compile("select last_insert_id.*"))
                .thenReturn(new JsonDataSetProducer("[ { \"LAST_INSERT_ID()\": 100 } ]").produce());

        dbServer.when()
                .statement()
                .executeQuery("select hibernate_sequence.nextval from dual - ()")
                .thenReturn(new JsonDataSetProducer("[ { \"HIBERNATE_SEQUENCE.NEXTVAL\": 100 } ]").produce());

        dbServer.when()
                .statement()
                .executeUpdate(Pattern.compile("insert into user .*"))
                .thenReturn(1);

        dbServer.when()
                .statement()
                .executeQuery("select user0_.id as id1_0_, user0_.email as email2_0_, user0_.name as name3_0_ from user user0_ - ()")
                .thenReturn(new JsonDataSetProducer(new ClassPathResource("users_hibernate.json").getFile()).produce());

        dbServer.when()
                .statement()
                .executeQuery(Pattern.compile("SELECT id, name FROM cities.*"))
                .thenReturn(new JsonDataSetProducer(new ClassPathResource("cities.json").getFile()).produce());

        dbServer.when()
                .statement()
                .execute(Pattern.compile("CALL findCityByName\\(\\?,:name\\) - \\(\\?,Munich\\)"))
                .thenReturn(new JsonDataSetProducer("[{ \"name\": \"Munich\", \"id\": 1 }]").produce());


        dbServer.start();
    }
}
