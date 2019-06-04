package com.lyz.springboot_neo4j.service.Algorithm;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

public class Community extends Importance{
    public static void SCC(Driver driver){
        String query = "CALL algo.scc('Person','Follow', {write:true,partitionProperty:'partition'})\n" +
                "YIELD loadMillis, computeMillis, writeMillis, setCount, maxSetSize, minSetSize;";

        StatementResult result = Importance.callalgo(query, driver);
        while(result.hasNext()){
            Record record = result.next();
            System.out.println(record);
        }
    }
}
