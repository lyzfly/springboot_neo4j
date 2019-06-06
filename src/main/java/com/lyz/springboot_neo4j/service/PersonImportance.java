package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import org.springframework.stereotype.Service;
import java.util.HashMap;


@Service
public class PersonImportance {
    Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "0228"));
    public double findOnePersonImportance(String name){
        String query = "CALL algo.degree.stream(\"Person\", \"In\", {direction: \"incoming\"})\n" +
                "YIELD nodeId, score\n" + "WHERE algo.asNode(nodeId).name={x}"+
                "RETURN score AS followers";
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                StatementResult result = tx.run(query, parameters("x",name));
                double degree = Double.parseDouble(result.next().get("followers").toString());
                return degree;
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

    public HashMap<String,String> findTheMostImport() {
        HashMap<String, String> hashMap = new HashMap<>();
        String query = "CALL algo.degree.stream(\"Person\",\"In\",{direction:\"incoming\"})\n" +
                "YIELD nodeId,score\n" + "RETURN algo.asNode(nodeId).name AS name,score AS followers\n" +
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
                return hashMap;
            }
        }
    }


}
