package com.lyz.springboot_neo4j.service.Algorithm;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

public class CallAlgorithm {

    public static StatementResult callalgo(String query, Driver driver){
        StatementResult result;
        try(Session session = driver.session()){
            try(Transaction tx = session.beginTransaction()){
                result = tx.run(query);
                tx.success();
            }
        }
        return result;
    }
}
