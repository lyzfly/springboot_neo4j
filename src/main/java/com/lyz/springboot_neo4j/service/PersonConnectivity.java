package com.lyz.springboot_neo4j.service;

import com.alibaba.fastjson.JSONObject;
import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.apache.commons.lang3.StringEscapeUtils;
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
        String query1 = String.format("MATCH(start:EXPERT{name:'%s',orgnizationname:'%s'}) RETURN start",startname,startorg);
        StatementResult result1 = neo4jUtil.excuteCypherSql(query1);
        String query2 = String.format("MATCH(end:EXPERT{name:'%s',orgnizationname:'%s'}) RETURN end",endname,endorg);
        StatementResult result2 = neo4jUtil.excuteCypherSql(query2);
        if(result1.hasNext()&&result2.hasNext()) {
            String query = String.format("MATCH (start:EXPERT{name:'%s',orgnizationname:'%s'}), " +
                    "(end:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                    "CALL algo.shortestPath.stream(start, end,'cost')\n" +
                    "YIELD nodeId, cost\n" +
                    "RETURN algo.asNode(nodeId).name AS name,algo.asNode(nodeId).orgnizationname as orgname,cost", startname, startorg, endname, endorg);
            StatementResult result = neo4jUtil.excuteCypherSql(query);
            List<Expert> list = new ArrayList<>();
            while (result.hasNext()) {
                JSONObject item = new JSONObject();
                Record record = result.next();
                String name = record.get("name").toString().replace("\"","");
                String orgname = record.get("orgname").toString().replace("\"","");
                item.put("name", name);
                item.put("orgnizationname", orgname);
                item_list.add(item);
                /*Expert expert = new Expert();
                expert.setName(name);
                expert.setOrgnizationname(orgname);
                list.add(expert);*/
            }
            re.put("expert_list", item_list);
            String tmp = StringEscapeUtils.unescapeEcmaScript(re.toJSONString());
            return tmp;
        }
        if(!result1.hasNext()&&!result2.hasNext()){
            return "起始节点不存在,终止节点不存在";
        }
        if(!result1.hasNext()){
            return "起始节点不存在";
        }
        if(!result2.hasNext()){
            return "终止节点不存在";
        }
        return null;
    }
}
