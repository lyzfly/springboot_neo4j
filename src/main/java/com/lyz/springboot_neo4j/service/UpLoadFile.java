package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpLoadFile {
    @Autowired
    Driver driver;

    public void loadcsvToNeo4j(){
        String query = "USING PERIODIC COMMIT 1000"+
                "LOAD CSV FROM 'file:///data.csv'"+
                "AS line MERGE(a:Person{name:line[0]})-[r:Follow]->(b:Person{name:line[1]})";
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(query);
                tx.success();
            }
        }
    }
}
