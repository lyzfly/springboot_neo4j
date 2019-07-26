package com.lyz.springboot_neo4j.service;

import com.alibaba.fastjson.JSONObject;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PersonCommunity {
    @Autowired
    Driver driver;

    @Autowired
    Neo4jUtil neo4jUtil;
    public String findOnePersonClass(){
        HashMap<String,String> hashMap = new HashMap<>();
        String query = "CALL algo.louvain.stream('EXPERT', 'EXPERT_COOPERATE_COUNT', {})\n" +
                "YIELD nodeId, community\n" +
                "\n" +
                "RETURN algo.asNode(nodeId).name AS name, community\n" +
                "ORDER BY community;";
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        while(result.hasNext()){
            Record record = result.next();
            String name = record.get("name").toString();
            String community = record.get("community").toString();
            hashMap.put(name, community);
        }
        String json = JSONObject.toJSONString(hashMap);
        return json;
    }
}
