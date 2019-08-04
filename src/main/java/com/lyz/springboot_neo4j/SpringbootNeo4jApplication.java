package com.lyz.springboot_neo4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringbootNeo4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootNeo4jApplication.class, args);
    }

}
