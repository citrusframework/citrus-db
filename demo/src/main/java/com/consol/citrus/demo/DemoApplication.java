package com.consol.citrus.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author Christop Deppisch
 */
@SpringBootApplication
public class DemoApplication {

    @Bean
    public DataSource dataSource(Environment environment) {
        return new DriverManagerDataSource(environment.getProperty("spring.datasource.url"),
                                                environment.getProperty("spring.datasource.username"),
                                                environment.getProperty("spring.datasource.password"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
