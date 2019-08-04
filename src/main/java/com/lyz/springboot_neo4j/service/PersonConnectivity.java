package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



/**
 shortestpath 路径发现
*/
@Service
public class PersonConnectivity {

    @Autowired
    Driver driver;

    @Autowired
    Neo4jUtil neo4jUtil;
    public List<Expert> ifConnective(String startname,String startorg,String endname,String endorg){
        boolean flag = false;
        String query = String.format("MATCH (start:EXPERT{name:'%s',orgnizationname:'%s'}), " +
                "(end:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                "CALL algo.shortestPath.stream(start, end,'cost')\n" +
                "YIELD nodeId, cost\n" +
                "RETURN algo.asNode(nodeId).name AS name,algo.asNode(nodeId).orgnizationname as orgname,cost",startname,startorg,endname,endorg);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        List<Expert> list = new ArrayList<>();
        while (result.hasNext()) {
            Record record = result.next();
            Expert expert = new Expert();
            String name = record.get("name").toString();
            String orgname = record.get("orgname").toString();
            expert.setName(name);
            expert.setOrgnizationname(orgname);
            System.out.println(name);
            list.add(expert);
        }
        System.out.println(list);
        return list;
    }
}
