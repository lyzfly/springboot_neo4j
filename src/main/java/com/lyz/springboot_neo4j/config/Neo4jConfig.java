package com.lyz.springboot_neo4j.config;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Neo4jConfig {

    @Value("${spring.data.neo4j.username}")
    String username;

    @Value("${spring.data.neo4j.password}")
    String password;

    @Value("${spring.data.neo4j.uri}")
    String url;
    @Bean
    public Driver f(){
        return GraphDatabase.driver(url, AuthTokens.basic(username, password));
    }
}
