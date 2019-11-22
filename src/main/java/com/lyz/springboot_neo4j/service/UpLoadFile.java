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
        System.out.println(load_rel);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(load_rel);
                tx.success();
            }
        }
    }

    public void delete_node(String[] expert_arr) {
        for (String expert : expert_arr) {
            String cypher = String.format("MATCH (a:EXPERT{id:'%s'}) DETACH DELETE a", expert);
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {
                    tx.run(cypher);
                    tx.success();
                }
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

