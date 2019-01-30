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
                .execute(Pattern.compile("CALL findCityByName \\(Munich, .*"))
                .thenReturn(new JsonDataSetProducer("[{ \"name\": \"Munich\", \"id\": 1 }]").produce());


        dbServer.start();
    }
}
