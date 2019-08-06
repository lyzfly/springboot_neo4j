package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhzy on 19-7-10.
 */
@Service
public class PersonSimilarity {

    @Autowired
    Neo4jUtil neo4jUtil;

    public List<Expert>  JaccordSim(String name,String orgname){
        List<Expert> list = new ArrayList<>();
        String query = String.format("MATCH (p1:EXPERT {name: '%s',orgnizationname:'%s'})-[:EXPERT_COOPERATE_COUNT]->(cuisine1)\n" +
                "WITH p1, collect(id(cuisine1)) AS p1Cuisine\n" +
                "MATCH (p2:EXPERT)-[:EXPERT_COOPERATE_COUNT]->(cuisine2) WHERE p1 <> p2\n" +
                "WITH p1, p1Cuisine, p2, collect(id(cuisine2)) AS p2Cuisine\n" +
                "RETURN p1.name AS from,\n" +
                "       p2.name AS to,\n" +
                "       algo.similarity.jaccard(p1Cuisine, p2Cuisine) AS similarity\n" +
                "ORDER BY similarity DESC",name,orgname);
        StatementResult result   = neo4jUtil.excuteCypherSql(query);
        Expert expert = new Expert();
        Record record = result.next();
        String similarnode = record.get("to").toString();
        String similar = record.get("similarity").toString();
        expert.setSimilarnode(similarnode);
        expert.setSimilarity(similar);
        list.add(expert);
        return list;
    }

    public List<Expert>  Euclidean(String name,String orgname){
        List<Expert> list = new ArrayList<>();
        String query = String.format("MATCH (p1:EXPERT {name: '%s',orgnizationname:'%s'})-[:EXPERT_COOPERATE_COUNT]->(cuisine1)\n" +
                "WITH p1, collect(id(cuisine1)) AS p1Cuisine\n" +
                "MATCH (p2:EXPERT)-[:EXPERT_COOPERATE_COUNT]->(cuisine2) WHERE p1 <> p2\n" +
                "WITH p1, p1Cuisine, p2, collect(id(cuisine2)) AS p2Cuisine\n" +
                "RETURN p1.name AS from,\n" +
                "       p2.name AS to,\n" +
                "       algo.similarity.jaccard(p1Cuisine, p2Cuisine) AS similarity\n" +
                "ORDER BY similarity DESC",name,orgname);
        StatementResult result   = neo4jUtil.excuteCypherSql(query);
        Expert expert = new Expert();
        Record record = result.next();
        String similarnode = record.get("to").toString();
        String similar = record.get("similarity").toString();
        expert.setSimilarnode(similarnode);
        expert.setSimilarity(similar);
        list.add(expert);
        return list;
    }
}
