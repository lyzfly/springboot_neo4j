package com.lyz.springboot_neo4j.service;

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

    public void loadcsvToNeo4j(String relfilename){
        String arr[] = relfilename.replace(".", "/").split("/");
        String nodename = arr[0];
        String relpath = "file:///"+relfilename;
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
        String relation = String.format("LOAD CSV FROM '%s' AS line MERGE(from:Person{name:line[0]}) MERGE(to:Person{name:line[1]}) MERGE(from)-[r:Follow]->(to)",relpath);
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                    tx.run(relation);
                    tx.success();
            }
        }
    }

    public String[] readitem(File filename) throws IOException {
        try (FileReader fr = new FileReader(filename)) {
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String arr[] = line.split(",");
            return arr;
        }
    }
}

