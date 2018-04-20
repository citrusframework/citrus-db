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

    public static void main(String[] args) throws IOException, SQLException {
        JdbcServer dbServer = new JdbcServer(args);

        dbServer.when()
                .statement()
                .executeQuery("select last_insert_id() - ()")
                .thenReturn(new JsonDataSetProducer("[ { \"LAST_INSERT_ID()\": 100 } ]").produce());

        dbServer.when()
                .statement()
                .executeQuery(Pattern.compile("SELECT id, name FROM cities.*"))
                .thenReturn(new JsonDataSetProducer(new ClassPathResource("cities.json").getFile()).produce());

        dbServer.when()
                .statement()
                .executeQuery("select user0_.id as id1_0_, user0_.email as email2_0_, user0_.name as name3_0_ from user user0_ - ()")
                .thenReturn(new JsonDataSetProducer(new ClassPathResource("users_hibernate.json").getFile()).produce());

        dbServer.start();
    }
}
