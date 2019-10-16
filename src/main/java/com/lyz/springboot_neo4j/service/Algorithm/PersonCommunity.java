package com.lyz.springboot_neo4j.service.Algorithm;

import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonCommunity {
    @Autowired
    Driver driver;

    @Autowired
    Neo4jUtil neo4jUtil;
    public List<Expert> louvain(String orgname,String name){
        List<Expert> list = new ArrayList<>();
        String query = String.format("MATCH (N:EXPERT) WHERE N.orgnizationname='%s' AND N.name='%s' Return N.community as community" ,orgname,name);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        int m = Integer.valueOf(result.next().get("community").toString());
        String query1 = String.format("MATCH (N:EXPERT) WHERE N.community=%d RETURN N.orgnizationname as orgname,N.name as name,N.community as community",m);
        StatementResult result1 = neo4jUtil.excuteCypherSql(query1);
        while(result1.hasNext()){
            Expert expert = new Expert();
            Record record = result1.next();
            String othername = record.get("name").toString();
            String otherorgname = record.get("orgname").toString();
            String community = record.get("community").toString();
            expert.setName(othername);
            expert.setOrgnizationname(otherorgname);
            expert.setCommunity(community);
            list.add(expert);
        }
        return list;
    }

    public List<Expert> lpa(String orgname,String name){
        List<Expert> list = new ArrayList<>();
        String query = String.format("MATCH (N:EXPERT) WHERE N.orgnizationname='%s' AND N.name='%s' Return N.partition as partition" ,orgname,name);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        int m = Integer.valueOf(result.next().get("partition").toString());
        String query1 = String.format("MATCH (N:EXPERT) WHERE N.partition=%d RETURN N.orgnizationname as orgname,N.name as name",m);
        StatementResult result1 = neo4jUtil.excuteCypherSql(query1);
        while(result1.hasNext()){
            Expert expert = new Expert();
            Record record = result1.next();
            String othername = record.get("name").toString();
            String otherorgname = record.get("orgname").toString();
            System.out.println(othername);
            System.out.println(otherorgname);
            expert.setName(othername);
            expert.setOrgnizationname(otherorgname);
            list.add(expert);
        }
        return list;
    }
}
