package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonCommunity {
    @Autowired
    Driver driver;

    public void findOnePersonClass(){
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                String query = "CALL algo.scc('Person','Follow', {write:true,partitionProperty:'partition'})\n" +
                        "YIELD loadMillis, computeMillis, writeMillis, setCount, maxSetSize, minSetSize;";
                StatementResult result = tx.run(query);
                while(result.hasNext()){
                    Record record = result.next();
                    
                }
                tx.success();
            }
        }
    }
}
