package com.lyz.springboot_neo4j.service;

import com.alibaba.fastjson.JSONObject;
import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.lang3.StringEscapeUtils;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import java.text.DecimalFormat;
import java.util.*;
/**
 * Created by zhzy on 19-7-10.
 */

@Service
public class PersonSimilarity {

    private static final Logger logger = org.apache.log4j.Logger.getLogger(PersonSimilarity.class);

    @Autowired
    Neo4jUtil neo4jUtil;

    public String  JaccordSim(String name,String orgname) {
        JSONObject re = new JSONObject();
        List item_list = new ArrayList();
        // List<Expert> list = new ArrayList<>();
        String query0 =  String.format("MATCH(n:EXPERT) where n.orgnizationname='%s' AND n.name='%s' RETURN n",orgname,name);
        StatementResult result0 = neo4jUtil.excuteCypherSql(query0);
        if(result0.hasNext()) {
            String query = String.format("MATCH (p1:EXPERT {name: '%s',orgnizationname:'%s'})-[r:rel]-(cuisine1)\n" +
                    "WITH p1, collect(id(cuisine1)) AS p1Cuisine\n" +
                    "MATCH(p2:EXPERT)-[r:rel]-(cuisine2)　WHERE p1 <> p2\n" +
                    "WITH p1, p1Cuisine, p2, collect(id(cuisine2)) AS p2Cuisine\n" +
                    "RETURN p1.name AS from,\n" +
                    "p2.name AS to,\n" + "p2.orgnizationname AS orgname," +
                    "algo.similarity.jaccard(p1Cuisine, p2Cuisine) AS similarity\n" +
                    "ORDER BY similarity DESC", name, orgname);
            System.out.println(query);
            StatementResult result = neo4jUtil.excuteCypherSql(query);
            Expert expert = new Expert();
            Record record = result.next();
            JSONObject reitem = new JSONObject();
            String similarnode_name = record.get("to").toString().replace("\"", "");
            String similarnode_org = record.get("orgname").toString().replace("\"", "");
            DecimalFormat df = new DecimalFormat("#.##");
            String similarity = df.format(Double.valueOf(record.get("similarity").toString()));
            reitem.put("similarnodename", similarnode_name);
            reitem.put("similarnodeorg", similarnode_org);
            reitem.put("similarity", similarity);
            item_list.add(reitem);
          /*  expert.setSimilarnodename(similarnode_name);
            expert.setSimilarnodeorg(similarnode_org);
            expert.setSimilarity(df.format(Double.valueOf(record.get("similarity").toString())));
            list.add(expert);*/
            re.put("expert_list", item_list);
            re.put("status","success");
        }else{
            re.put("status","fail");
        }
        String tmp = StringEscapeUtils.unescapeEcmaScript(re.toJSONString());
        return tmp;
    }

    public String DegreeSim(String name,String orgname){
        JSONObject re = new JSONObject();
        List item_list = new ArrayList();
        List<Expert> list = new ArrayList<>();
        List<Expert> list1 = new ArrayList<>();
        String query0 = String.format("match (n:EXPERT{name:'%s',orgnizationname:'%s'}) Return n",name,orgname);
        StatementResult result0 = neo4jUtil.excuteCypherSql(query0);
        if(result0.hasNext()){
            String query1 = String.format("match (n:EXPERT{name:'%s',orgnizationname:'%s'})"+
                    "-[r:rel]-(target) match (m)-[:rel]-(target) return m.name as name," +
                    "m.orgnizationname as orgname,m.id as id",name,orgname);
            StatementResult result = neo4jUtil.excuteCypherSql(query1);
            List<String> id_list = new ArrayList<String>();
            while(result.hasNext()){
                Record record = result.next();
                if(!id_list.contains(record.get("id").toString())){
                    id_list.add(record.get("id").toString());
                    Expert expert = new Expert();
                    String expert_name = record.get("name").toString();
                    String expert_orgname = record.get("orgname").toString();
                    expert.setName(expert_name);
                    expert.setOrgnizationname(expert_orgname);
                    list1.add(expert);
                }

            }
            HashMap<Expert, Integer> hashMap = new HashMap<Expert, Integer>();
            for(int i=0;i<list1.size();i++) {
                String expert_name = list1.get(i).getName();
                String expert_orgname = list1.get(i).getOrgnizationname();
                String str1 = expert_name.replace("\"", "");
                String str2 = expert_orgname.replace("\"", "");
                String query = String.format("match (n:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                        "--(target)--(m:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                        " return count(distinct target) as num", name, orgname, str1, str2);
                StatementResult result1 = neo4jUtil.excuteCypherSql(query);
                int count = Integer.valueOf(result1.next().get("num").toString());
                hashMap.put(list1.get(i), count);
            }
            List<Map.Entry<Expert, Integer>> list_Data = new ArrayList<>(hashMap.entrySet());
            Collections.sort(list_Data, new Comparator<Map.Entry<Expert, Integer>>()
            {
                @Override
                public int compare(Map.Entry<Expert, Integer> o1, Map.Entry<Expert, Integer> o2) {
                    if (o2.getValue() != null && o1.getValue() != null && o2.getValue().compareTo(o1.getValue()) > 0) {

                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            Queue<Integer> queue = new LinkedList<>();
            for(Map.Entry<Expert,Integer> item : list_Data){
                JSONObject reitem = new JSONObject();
                if(item.getKey().getName().replace("\"","").equals(name)){
                    continue;
                }
                if(queue.isEmpty()||queue.peek()==item.getValue()){
                    DecimalFormat df = new DecimalFormat("#.00");
                    String similarnode_name = item.getKey().getName().replace("\"","");
                    String similarnode_org = item.getKey().getOrgnizationname().replace("\"","");
                    String similarity = df.format(item.getValue());
                    reitem.put("similarnodename",similarnode_name);
                    reitem.put("similarnodeorg",similarnode_org);
                    reitem.put("similarity",similarity);
                    queue.add(item.getValue());
                   /* Expert expert = new Expert();
                    expert.setSimilarnodename(item.getKey().getName());
                    expert.setSimilarnodeorg(item.getKey().getOrgnizationname());
                    expert.setSimilarity(df.format(item.getValue()));
                    list.add(expert);*/
                }else if(queue.peek()!=item.getValue()){
                    break;
                }
                item_list.add(reitem);
            }
            re.put("expert_list",item_list);
            re.put("status","success");
        }else{
            re.put("status","fail");
        }
        String tmp = StringEscapeUtils.unescapeEcmaScript(re.toJSONString());
        return tmp;
    }
}