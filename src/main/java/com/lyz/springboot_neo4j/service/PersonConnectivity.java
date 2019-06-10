package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;
@Service
public class PersonConnectivity {

    @Autowired
    Driver driver;
    public List<String> ifConnective(String start,String end){
        boolean flag = false;
        String query = "MATCH (start:Person{name:{x}}), (end:Person{name:{y}})\n" +
                "CALL algo.shortestPath.stream(start, end,'cost')\n" +
                "YIELD nodeId, cost\n" +
                "RETURN algo.asNode(nodeId).name AS name, cost";
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                StatementResult result = tx.run(query,parameters("x",start,"y",end));
                List<String> list = new ArrayList<String>();
                if(result.hasNext()){
                    Record record = result.next();
                    String name = record.get("name").toString();
                    System.out.println(name);
                    list.add(name);
                    flag = true;
                }
                tx.success();
                return list;
            }
        }
    }

}
