package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteGraph {
    @Autowired
    Driver driver;
    public void delete(){
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run("MATCH (a:Person) DETACH DELETE a");
                tx.success();
            }
        }
    }
}
