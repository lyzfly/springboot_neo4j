package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.config.Neo4jConfig;
import com.lyz.springboot_neo4j.service.UpLoadFile;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class test {
@Autowired
Driver driver = GraphDatabase.driver("bolt://localhost:7687",AuthTokens.basic("neo4j", "0228"));
    public void f(){
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                StatementResult result = tx.run("MATCH(n:Person) RETURN n");
                List<Record> list = result.list();
                for(Record record:list){
                    List<Pair<String,Value>> list1 = record.fields();
                    for(Pair<String,Value> f:list1){
                        System.out.println();
                        String name = f.value().type().name();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String filename =  "relation.csv";
        String arr[] = filename.replace(".","/" ).split("/");
        System.out.println(arr[1]);
        new test().f();

    }
}
