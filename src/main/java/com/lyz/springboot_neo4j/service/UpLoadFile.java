package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.neo4j.driver.v1.Values.parameters;
@Service
public class UpLoadFile {
    @Autowired
    Driver driver;

    public void loadcsvToNeo4j(String nodefilename,String relfilename){
        String nodepath = "file:///"+ nodefilename;
        String relpath = "file:///"+relfilename;
        String query_node =
                "LOAD CSV FROM {nodepath}"+
                "AS line Merge(a:Person{name:line[0]})";
        String query_relation = "LOAD CSV FROM {relpath}"+"AS line Match(from:Person{name:line[0]}),"
                +"(to:Person{name:line[1]})"+"MERGE(from)-[r:Follow]->(to)";
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                tx.run(query_node,parameters("nodepath",nodepath));
                tx.run(query_relation,parameters("relpath",relpath));
                tx.success();
            }
        }

    }

}
