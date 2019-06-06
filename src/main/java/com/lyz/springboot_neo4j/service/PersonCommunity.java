package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.*;
import org.springframework.stereotype.Service;

@Service
public class PersonCommunity {

    Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "0228"));

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
