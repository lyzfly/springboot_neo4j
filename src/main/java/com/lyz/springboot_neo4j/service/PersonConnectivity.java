package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.neo4j.driver.v1.Values.parameters;
@Service
public class PersonConnectivity {

    @Autowired
    Driver driver;
    public boolean ifConnective(String start,String end){
        boolean flag = false;
        String query = "MATCH (start:Person{name:{x}}), (end:Person{name:{y}})\n" +
                "CALL algo.shortestPath.stream(start, end,'cost')\n" +
                "YIELD nodeId, cost\n" +
                "RETURN algo.asNode(nodeId).name AS name, cost";
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                StatementResult result = tx.run(query,parameters("x",start,"y",end));
                if(result.hasNext()){
                    flag = true;
                }
                tx.success();
            }
        }
        return flag;
    }

}
