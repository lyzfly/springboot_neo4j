package com.lyz.springboot_neo4j.service;

import com.alibaba.fastjson.JSON;
import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class PersonImportance {
    @Autowired
    Driver driver;
    @Autowired
    Neo4jUtil neo4jUtil;
    public String findOnePersonImportance(String name,String orgnizationname){
        HashMap<String,Object> hashMap;
        String query = null;
        query = String.format("MATCH (u1:EXPERT)<-[r:EXPERT_COOPERATE_COUNT]-(u2:EXPERT) " +
                "WHERE u1.name='%s' AND u1.orgnizationname='%s' RETURN *",name,orgnizationname);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        hashMap = neo4jUtil.GetGraphNodeAndShip(result);
        String re= JSON.toJSONString(hashMap);
        return re;
    }

   /* public HashMap<String,String> findImportance() {
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
    }*/

    public List<Expert> DegreeMostImportant(String orgname, int cnt) {
        List<Expert> list = new ArrayList<>();
        String query;
        query = String.format("CALL algo.degree.stream(\"EXPERT\",\"In\",{direction:\"incoming\"})\n" +
                "YIELD nodeId,score\n" +"WHERE algo.asNode(nodeId).orgnizationname='%s'"+
                "RETURN algo.asNode(nodeId).name AS name," +"algo.asNode(nodeId).orgnizationid AS orgid,"+
                "algo.asNode(nodeId).orgnizationname as orgname,score AS followers\n" +
                "ORDER BY followers DESC",orgname);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        double before = Double.MIN_VALUE;
        int count = 0;
        while (result.hasNext()&&count<cnt) {
            Record record = result.next();
            Expert expert = new Expert();
            String name = record.get("name").toString();
            String id = record.get("orgid").toString();
            Double followers = Double.valueOf(record.get("followers").toString());
            expert.setName(name);
            expert.setOrgnizationname(orgname);
            expert.setImportance(followers);
            expert.setOrgnizationid(id);
            if (followers != before) {
                count++;
            }
            list.add(expert);
            before = followers;
        }
        return list;
    }

    public HashMap<String,Double> PageRankMostImportant(String orgname,int cnt){
        HashMap<String,Double> hashMap = new HashMap<>();
        String query;
        query = String.format("CALL algo.pageRank.stream('EXPERT', 'EXPERT_COOPERATE_COUNT', " +
                "{iterations:20, dampingFactor:0.85})\n" +
                "YIELD nodeId, score\n" + "WHERE algo.asNode(nodeId).orgnizationname='%s'"+
                "RETURN algo.asNode(nodeId).name AS name,algo.asNode(nodeId).orgnizationname as orgname,score\n" +
                "ORDER BY score DESC",orgname);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        double before = Double.MIN_VALUE;
        int count = 0;
        while(result.hasNext()&&count<cnt){
            Record record = result.next();
            Double value = Double.valueOf(record.get("score").toString());
            String key = record.get("name").toString()+record.get("orgname").toString();
            if(before!=value){
                count++;
            }
            hashMap.put(key,value);
            before = value;
        }
        return hashMap;
    }
}
