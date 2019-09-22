package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
public class UpLoadFile {
    @Autowired
    Driver driver;
    Neo4jUtil neo4jUtil;

    public void loadcsvToNeo4j_node(String nodefilename){
        String nodefile = "file:///"+ nodefilename;

        /*int title_len = title.length;
        System.out.println(title_len);

        String query1 = String.format("LOAD CSV WITH HEADERS FROM '%s' AS line MERGE(n:%s{%s:line.%s})",relpath,nodename,title[0],title[0]);
        String query2 = String.format("LOAD CSV WITH HEADERS FROM '%s' AS line MERGE(n:%s{%s:line.%s,%s:line.%s})",relpath,nodename,title[0],title[0],title[1],title[1]);
        String query3 = String.format("LOAD CSV WITH HEADERS FROM '%s' AS line MERGE(n:%s{%s:line.%s,%s:line.%s,%s:line.%s})",relpath,nodename,title[0],title[0],title[1],title[1],title[2],title[2]);
              *//*  +"MERGE(to:Person{name:line[1]})"
                +"MERGE(from)-[r:Follow]->(to)";*//*
        //String index = "CREATE INDEX ON:Person(name)";
        String cyphersql = "";
        if(title_len==1){
            cyphersql = query1;
        }else if(title_len==2){
            cyphersql = query2;
        }else if(title_len==3){
            cyphersql = query3;
        }*/
        String load_node = String.format("LOAD CSV WITH HEADERS FROM '%s' AS line " +
                "MERGE(n:EXPERT{id:line.id,name:line.name,orgnizationid:line.orgnizationid,orgnizationname:line.orgnizationname})",nodefile);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(load_node);
                tx.success();
            }
        }
    }

    public void loadcsvToNeo4j_rel(String relfilename){
        String relfile = "file:///"+relfilename;
        String load_rel = String.format("LOAD CSV WITH HEADERS FROM '%s' AS line MATCH(from:EXPERT{id:line.fromid})," +
                "(to:EXPERT{id:line.toid}) MERGE(from)-[r:rel{relation:line.relation}]-(to)",relfile);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(load_rel);
                tx.success();
            }
        }
    }

    public void delete_node(String nodeid){

        String cypher = String.format("MATCH (a:EXPERT{id:'%s'}) DETACH DELETE a",nodeid);
        System.out.println(cypher);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(cypher);
                tx.success();
            }
        }
    }

    public void delete_edge(String startnodeid,String endnodeid){

        String cypher = String.format("MATCH (a:EXPERT{id:'%s'})-[r:rel]-(b:EXPERT{id:'%s'}) DELETE r",startnodeid,endnodeid);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(cypher);
                tx.success();
            }
        }
    }

}

