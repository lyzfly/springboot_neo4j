package com.lyz.springboot_neo4j.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
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

    public String DegreeMostImportant(String orgname, int cnt) {
        JSONObject re = new JSONObject();
        List item_list = new ArrayList();
        List<Expert> list = new ArrayList<>();
        String query;
        query = String.format("CALL algo.degree.stream(\"EXPERT\",\"In\",{direction:\"both\"})\n" +
                "YIELD nodeId,score\n" +"WHERE algo.asNode(nodeId).orgnizationname='%s'"+
                "RETURN algo.asNode(nodeId).name AS name," +"algo.asNode(nodeId).orgnizationid AS orgid,"+
                "algo.asNode(nodeId).orgnizationname as orgname,score AS followers\n" +
                "ORDER BY followers DESC",orgname);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        double before = Double.MIN_VALUE;
        int count = 0;
        while (result.hasNext()&&count<cnt) {
            JSONObject reitem = new JSONObject();
            Record record = result.next();
            Expert expert = new Expert();
            String name = record.get("name").toString();
            String id = record.get("orgid").toString();
            Double followers = Double.valueOf(record.get("followers").toString());
            expert.setName(name);
            expert.setOrgnizationname(orgname);
            expert.setImportance(followers);
            expert.setOrgnizationid(id);
            reitem.put("name",name);
            reitem.put("followers",followers);
            if (followers != before) {
                count++;
                System.out.println("count"+count);
            }
            before = followers;
            item_list.add(reitem.toJSONString());
            list.add(expert);
        }
        re.put("expert_list",item_list);
        return re.toJSONString();
    }

    public List<Expert> PageRankMostImportant(String orgname,int cnt){
        List<Expert> list = new ArrayList<>();
        String query;
        query = String.format("CALL algo.pageRank.stream('EXPERT', 'rel', " +
                "{iterations:20, dampingFactor:0.85})\n" +
                "YIELD nodeId, score\n" + "WHERE algo.asNode(nodeId).orgnizationname='%s'"+
                "RETURN algo.asNode(nodeId).name AS name,algo.asNode(nodeId).orgnizationname as orgname,score\n" +
                "ORDER BY score DESC",orgname);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        int count=0;
        double before = Double.MIN_VALUE;
        while(result.hasNext()&&count<cnt){
            Record record = result.next();
            Expert expert = new Expert();
            String name = record.get("name").toString();
            Double importance = Double.valueOf(record.get("score").toString());
            DecimalFormat df = new DecimalFormat("#.00");
            importance = Double.valueOf(df.format(importance));
            System.out.println(name);
            System.out.println(importance);
            expert.setName(name);
            expert.setImportance(importance);
            list.add(expert);
            if(importance!=before){
                count++;
            }
            before = importance;
        }
        return list;
    }

    public List<Expert> Betweenness(String orgname,int cnt){
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
            before = followers;
            list.add(expert);
        }
        return list;
    }
}
