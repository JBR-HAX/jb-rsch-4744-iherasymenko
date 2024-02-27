package org.jetbrains.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

@SpringBootApplication(proxyBeanMethods = false)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Sql2o sql2o(DataSource ds) {
        return new Sql2o(ds);
    }
}
