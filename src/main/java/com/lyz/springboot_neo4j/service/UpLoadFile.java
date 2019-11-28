package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpLoadFile {
    @Autowired
    Driver driver;
    @Autowired
    Neo4jUtil neo4jUtil;

    public void loadcsvToNeo4j_node(String nodefile){
        //String nodefile = "file:///"+ nodefilename;

        String load_node = String.format("LOAD CSV WITH HEADERS FROM '%s' AS line " +
                "MERGE(n:EXPERT{id:line.id,name:line.name,orgnizationid:line.orgnizationid,orgnizationname:line.orgnizationname})",nodefile);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(load_node);
                tx.success();
            }
        }
    }

    public void loadcsvToNeo4j_rel(String relfile){
        String load_rel = String.format("LOAD CSV WITH HEADERS FROM '%s' AS line MATCH(from:EXPERT{id:line.fromid})," +
                "(to:EXPERT{id:line.toid}) MERGE(from)-[r:rel{relation:line.relation}]->(to)",relfile);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(load_rel);
                tx.success();
            }
        }
    }

    public String delete_node(String[] expert_arr) {
        boolean flag = true;
        String false_line = "删除失败，不存在节点";
        for (String expert : expert_arr) {
            String cypher0 = String.format("match(n:EXPERT{id:'%s'}) return n", expert);
            StatementResult result0 = neo4jUtil.excuteCypherSql(cypher0);
            String cypher = String.format("match(n:EXPERT{id:'%s'}) detach delete n", expert);
            StatementResult result = neo4jUtil.excuteCypherSql(cypher);
            if(!result0.hasNext()){
                flag = false;
            }
            false_line  = false_line + expert + " ";
        }
        if(!flag){
            return false_line;
        }else{
            return "删除成功！";
        }
    }

    public String delete_edge(String startnodeid,String endnodeid){

        String cypher0 = String.format("MATCH (a:EXPERT{id:'%s'})-[r:rel]-(b:EXPERT{id:'%s'}) RETURN r",startnodeid,endnodeid);
        StatementResult result = neo4jUtil.excuteCypherSql(cypher0);
        if(!result.hasNext()){
            return "删除边失败！";
        }
        String cypher = String.format("MATCH (a:EXPERT{id:'%s'})-[r:rel]-(b:EXPERT{id:'%s'}) DELETE r",startnodeid,endnodeid);
        System.out.println(cypher);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(cypher);
                tx.success();
            }
        }
        return "删除成功！";
    }
}

