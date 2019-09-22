package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        String query = String.format("MATCH (p1:EXPERT {name: '%s',orgnizationname:'%s'})-[r:rel]->(cuisine1)\n" +
                "WITH p1, collect(id(cuisine1)) AS p1Cuisine\n" +
                "WITH p1, collect(id(cuisine1)) AS p1Cuisine\n" +
                "WITH p1, p1Cuisine, p2, collect(id(cuisine2)) AS p2Cuisine\n" +
                "RETURN p1.name AS from,\n" +
                "       p2.name AS to,\n" + "p2.orgnizationname AS orgname," +
                "       algo.similarity.jaccard(p1Cuisine, p2Cuisine) AS similarity\n" +
                "ORDER BY similarity DESC",name,orgname);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        Expert expert = new Expert();
        Record record = result.next();
        String similarnode_name = record.get("to").toString();
        String similarnode_org = record.get("orgname").toString();
        String similar = record.get("similarity").toString();
        expert.setSimilarnodename(similarnode_name);
        expert.setSimilarnodeorg(similarnode_org);
        expert.setSimilarity(similar);
        list.add(expert);
        return list;
    }

    public List<Expert> DegreeSim(String name,String orgname){
        int max = Integer.MIN_VALUE;
        String to_name = null;
        String to_orgname = null;
        String query1 = String.format("match (n:EXPERT{name:'%s',orgnizationname:'%s'})"+
                "-[r:rel]-(target) match (m)-[:rel]-(target) return m.name as name," +
                "m.orgnizationname as orgname",name,orgname);
        StatementResult result = neo4jUtil.excuteCypherSql(query1);
        List<Expert> list1 = new ArrayList<>();
        while(result.hasNext()){
            Record record = result.next();
            Expert expert = new Expert();
            String expert_name = record.get("name").toString();
            String expert_orgname = record.get("orgname").toString();
            expert.setName(expert_name);
            expert.setOrgnizationname(expert_orgname);
            list1.add(expert);
        }
        for(int i=0;i<list1.size();i++){
            String expert_name = list1.get(i).getName();
            String expert_orgname = list1.get(i).getOrgnizationname();
            String str1= expert_name.replace("\"", "");
            String str2= expert_orgname.replace("\"", "");
            String query = String.format("match (n:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                    "--(target)--(m:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                    " return count(target) as num",name,orgname,str1,str2);
            StatementResult result1 = neo4jUtil.excuteCypherSql(query);
            int count = Integer.valueOf(result1.next().get("num").toString());
            if(max<count){
                max = count;
                to_name = expert_name;
                to_orgname = expert_orgname;
            }
        }
        List<Expert> list = new ArrayList<>();
        Expert expert = new Expert();
        expert.setSimilarnodename(to_name);
        expert.setSimilarnodeorg(to_orgname);
        expert.setSimilarity(String.valueOf(max));
        list.add(expert);
        return list;
    }

}