package com.lyz.springboot_neo4j.service;

import com.alibaba.fastjson.JSON;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;


@Service
public class PersonImportance {
    @Autowired
    Driver driver;
    @Autowired
    Neo4jUtil neo4jUtil;
    public String findOnePersonImportance(String name){
        HashMap<String,Object> hashMap;
        String query = "MATCH (u1:Person)<-[r:Follow]-(u2:Person) WHERE u1.name={x} RETURN *";
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                StatementResult result = tx.run(query, parameters("x",name));
                hashMap = neo4jUtil.GetGraphNodeAndShip(result);
                String re= JSON.toJSONString(hashMap);
                return re;
            }
        }
    }

    public HashMap<String,String> findImportance() {
        HashMap<String, String> hashMap = new HashMap<>();
        String query = "CALL algo.degree.stream(\"Person\", \"In\", {direction: \"incoming\"})\n" +
                "YIELD nodeId, score\n" +
                "RETURN algo.asNode(nodeId).name AS name, score AS followers\n" +
                "ORDER BY followers DESC";
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run(query);
                tx.success();
                while (result.hasNext()) {
                    Record record = result.next();
                    String key = record.get("name").toString();
                    String value = record.get("followers").toString();
                    hashMap.put(key, value);
                }
            }
            return hashMap;
        }
    }

    public HashMap<String,Double> findTheMostImport() {
        HashMap<String, Double> hashMap = new HashMap<>();
        String query = "CALL algo.degree.stream(\"Person\",\"In\",{direction:\"incoming\"})\n" +
                "YIELD nodeId,score\n" + "RETURN algo.asNode(nodeId).name AS name,score AS followers\n" +
                "ORDER BY followers DESC";
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run(query);
                double max = Double.MIN_VALUE;
                while (result.hasNext()) {
                    Record record = result.next();
                    Double value = Double.valueOf(record.get("followers").toString());
                    String key = record.get("name").toString();
                    if (max <= value) {
                        max = value;
                        hashMap.put(key, value);
                    } else {
                        break;
                    }
                }
            }
        }
        return hashMap;
    }
}
