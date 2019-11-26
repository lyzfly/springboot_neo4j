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

@Service
public class PersonCommunity {

    @Autowired
    Neo4jUtil neo4jUtil;
    public String louvain(String orgname,String name){

        //List<Expert> list = new ArrayList<>();
        JSONObject re = new JSONObject();
        List item_list = new ArrayList();

        String query = String.format("MATCH (N:EXPERT) WHERE N.orgnizationname='%s' AND N.name='%s' Return N.community as community" ,orgname,name);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        if(result.hasNext()) {
            int m = Integer.valueOf(result.next().get("community").toString());
            String query1 = String.format("MATCH (N:EXPERT) WHERE N.community=%d RETURN N.orgnizationname as orgname,N.name as name,N.community as community", m, name);
            StatementResult result1 = neo4jUtil.excuteCypherSql(query1);
            while (result1.hasNext()) {
                JSONObject item = new JSONObject();

                Record record = result1.next();
                String othername = record.get("name").toString().replace("\"","");
                String otherorgname = record.get("orgname").toString().replace("\"","");
                String community = record.get("community").toString();
               /* Expert expert = new Expert();
                expert.setName(othername);
                expert.setOrgnizationname(otherorgname);
                expert.setCommunity(community);*/
                item.put("name", othername);
                item.put("orgnizationname", otherorgname);
                item.put("community", community);
                item_list.add(item);
                //list.add(expert);
            }
            re.put("expert_list", item_list);
            String tmp = StringEscapeUtils.unescapeEcmaScript(re.toJSONString());
            return tmp;
        }else{
            return "false";
        }
    }

    public String lpa(String orgname,String name){
        //List<Expert> list = new ArrayList<>();
        List item_list = new ArrayList<>();
        JSONObject re = new JSONObject();

        String query = String.format("MATCH (N:EXPERT) WHERE N.orgnizationname='%s' AND N.name='%s' Return N.partition as partition" ,orgname,name);
        StatementResult result = neo4jUtil.excuteCypherSql(query);
        if(result.hasNext()) {
            int m = Integer.valueOf(result.next().get("partition").toString());
            String query1 = String.format("MATCH (N:EXPERT) WHERE N.partition=%d  RETURN N.orgnizationname as orgname,N.name as name", m, name);
            StatementResult result1 = neo4jUtil.excuteCypherSql(query1);
            while (result1.hasNext()) {
                JSONObject item = new JSONObject();
                Record record = result1.next();
                String othername = record.get("name").toString().replace("\"","");
                String otherorgname = record.get("orgname").toString().replace("\"","");
                item.put("name",othername );
                item.put("orgnizationname", otherorgname);
                item_list.add(item);
                /*Expert expert = new Expert();
                expert.setName(othername);
                expert.setOrgnizationname(otherorgname);
                list.add(expert);*/
            }
            re.put("expert_list", item_list);
            String tmp = StringEscapeUtils.unescapeEcmaScript(re.toJSONString());
            return tmp;
        }else{
            return "false";
        }
    }
}
