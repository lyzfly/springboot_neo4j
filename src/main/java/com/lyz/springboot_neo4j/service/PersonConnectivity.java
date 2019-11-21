package com.lyz.springboot_neo4j.service;

import com.alibaba.fastjson.JSONObject;
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
    Neo4jUtil neo4jUtil;
    public String ifConnective(String startname,String startorg,String endname,String endorg){
        JSONObject re = new JSONObject();
        List item_list = new ArrayList();
        String query = String.format("MATCH (start:EXPERT{name:'%s',orgnizationname:'%s'}), " +
                "(end:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                "CALL algo.shortestPath.stream(start, end,'cost')\n" +
                "YIELD nodeId, cost\n" +
                "RETURN algo.asNode(nodeId).name AS name,algo.asNode(nodeId).orgnizationname as orgname,cost",startname,startorg,endname,endorg);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        List<Expert> list = new ArrayList<>();
        while (result.hasNext()) {
            JSONObject item = new JSONObject();
            Record record = result.next();
            String name = record.get("name").toString();
            String orgname = record.get("orgname").toString();
            item.put("name",name);
            item.put("orgnizationname",orgname);
            item_list.add(item);
            /*Expert expert = new Expert();
            expert.setName(name);
            expert.setOrgnizationname(orgname);
            list.add(expert);*/
        }
        re.put("expert_list",item_list);
        return re.toJSONString();
    }
}
