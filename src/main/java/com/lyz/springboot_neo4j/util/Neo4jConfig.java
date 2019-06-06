package com.lyz.springboot_neo4j.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Neo4jConfig {

    @Value("${spring.data.neo4j.username}")
    String username;

    @Value("${spring.data.neo4j.password}")
    String password;

    @Value("${spring.data.neo4j.uri}")
    String url;


    public void f(){
        System.out.println(username+" "+password);
    }
    public static void main(String[] args) {
        new Neo4jConfig().f();
    }

}
