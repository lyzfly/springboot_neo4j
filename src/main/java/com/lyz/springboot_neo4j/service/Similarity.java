package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Driver;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhzy on 19-7-10.
 */
public class Similarity {
    @Autowired
    Driver driver;
    @Autowired
    Neo4jUtil neo4jUtil;

    public void  FindTheMostReletive(){
        String query = "Match(n:Person)<-[:Follow]-(m:Person)";
        StatementResult statementResult   = neo4jUtil.excuteCypherSql(query);
        int n = statementResult.list().size();
        List<Record> records = statementResult.list();
        for(Record record: records){
        }
    }
}
