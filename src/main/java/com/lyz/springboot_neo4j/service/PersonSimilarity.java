package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
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

    public List<Expert>  JaccordSim(String name,String orgname){
        List<Expert> list = new ArrayList<>();
        try {
            String query = String.format("MATCH (p1:EXPERT {name: '%s',orgnizationname:'%s'})-[r:rel]-(cuisine1)\n" +
                    "WITH p1, collect(id(cuisine1)) AS p1Cuisine\n" +
                    "MATCH(p2:EXPERT)-[r:rel]-(cuisine2)　WHERE p1 <> p2\n"+
                    "WITH p1, p1Cuisine, p2, collect(id(cuisine2)) AS p2Cuisine\n" +
                    "RETURN p1.name AS from,\n" +
                    "p2.name AS to,\n" + "p2.orgnizationname AS orgname," +
                    "algo.similarity.jaccard(p1Cuisine, p2Cuisine) AS similarity\n" +
                    "ORDER BY similarity DESC",name,orgname);
            StatementResult result = neo4jUtil.excuteCypherSql(query);
            Expert expert = new Expert();
            Record record = result.next();
            String similarnode_name = record.get("to").toString();
            System.out.println(similarnode_name);
            String similarnode_org = record.get("orgname").toString();
            System.out.println(similarnode_org);
            DecimalFormat df = new DecimalFormat("#.##");
            expert.setSimilarnodename(similarnode_name);
            expert.setSimilarnodeorg(similarnode_org);
            expert.setSimilarity(df.format(Double.valueOf(record.get("similarity").toString())));
            list.add(expert);
        }catch (Exception e){
            logger.info("节点不存在！");
            System.out.println("节点不存在！");
            return list;
        }
        return list;
    }

    public List<Expert> DegreeSim(String name,String orgname){
        List<Expert> list = new ArrayList<>();
        List<Expert> list1 = new ArrayList<>();
        try {
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
            System.out.println("listsize"+list1.size());
            HashMap<Expert, Integer> hashMap = new HashMap<Expert, Integer>();
            for(int i=0;i<list1.size();i++) {
                String expert_name = list1.get(i).getName();
                String expert_orgname = list1.get(i).getOrgnizationname();
                String str1 = expert_name.replace("\"", "");
                String str2 = expert_orgname.replace("\"", "");
                System.out.println(expert_name);
                System.out.println(expert_orgname);
                String query = String.format("match (n:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                        "--(target)--(m:EXPERT{name:'%s',orgnizationname:'%s'})\n" +
                        " return count(distinct target) as num", name, orgname, str1, str2);
                System.out.println(query);
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
                System.out.println(list_Data);
                Queue<Integer> queue = new LinkedList<>();
                for(Map.Entry<Expert,Integer> item : list_Data){
                    System.out.println("name"+item.getKey().getName());
                    System.out.println("name1"+name);
                    if(item.getKey().getName().replace("\"","").equals(name)){
                        continue;
                    }
                    if(queue.isEmpty()||queue.peek()==item.getValue()){
                        queue.add(item.getValue());
                        Expert expert = new Expert();
                        expert.setSimilarnodename(item.getKey().getName());
                        expert.setSimilarnodeorg(item.getKey().getOrgnizationname());
                        DecimalFormat df = new DecimalFormat("#.00");
                        expert.setSimilarity(df.format(item.getValue()));
                        list.add(expert);
                    }else if(queue.peek()!=item.getValue()){
                        break;
                    }
                }
        }catch (Exception e){
            logger.info("节点不存在！");
        }
        return list;
    }
}