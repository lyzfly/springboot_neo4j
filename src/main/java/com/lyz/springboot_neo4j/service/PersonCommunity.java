package com.lyz.springboot_neo4j.service;

import com.alibaba.fastjson.JSONObject;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PersonCommunity {
    @Autowired
    Driver driver;

    public String findOnePersonClass(){
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                HashMap<String,String> hashMap = new HashMap<>();
                String query = "CALL algo.louvain.stream('Person', 'Follow', {})\n" +
                        "YIELD nodeId, community\n" +
                        "\n" +
                        "RETURN algo.asNode(nodeId).name AS name, community\n" +
                        "ORDER BY community;";
                StatementResult result = tx.run(query);
                while(result.hasNext()){
                    Record record = result.next();
                    String name = record.get("name").toString();
                    String community = record.get("community").toString();
                    hashMap.put(name, community);
                }
                tx.success();
                String json = JSONObject.toJSONString(hashMap);
                return json;
            }
        }
    }
}
