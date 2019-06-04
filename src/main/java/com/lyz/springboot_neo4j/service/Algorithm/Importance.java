package com.lyz.springboot_neo4j.service.Algorithm;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

public class Importance extends  CallAlgorithm{

    public static void Degree(Driver driver){
        String query = "CALL algo.degree.stream(\"Person\", \"In\", {direction: \"incoming\"})\n" +
                "YIELD nodeId, score\n" +
                "RETURN algo.asNode(nodeId).name AS name, score AS followers\n" +
                "ORDER BY followers DESC";
        StatementResult result = Importance.callalgo(query,driver);
        while(result.hasNext()){
            Record record = result.next();
            System.out.println(record.get("name")+" "+record.get("followers"));
        }
    }

    public static void PageRank(Driver driver){

        String query = "CALL algo.pageRank.stream('Person', 'Follow', {iterations:20, dampingFactor:0.85})\n" +
                "YIELD nodeId, score\n" +
                "\n" +
                "RETURN algo.asNode(nodeId).name AS name,score\n" +
                "ORDER BY score DESC";

        StatementResult result = Importance.callalgo(query, driver);
        while(result.hasNext()){
            Record record = result.next();
            System.out.println(record.get("name")+" "+record.get("score"));
        }
    }

    /**
     * 适用于连通图
     * @param driver
     */
    public static void ClosenestCentrality(Driver driver){
        String query = "CALL algo.closeness.stream('Person', 'Follow')\n" +
                "YIELD nodeId, centrality\n" +
                "\n" +
                "RETURN algo.asNode(nodeId).id AS name, centrality\n" +
                "ORDER BY centrality DESC\n" +
                "LIMIT 20;";
        StatementResult result = Importance.callalgo(query, driver);
        while(result.hasNext()){
            Record record = result.next();
            System.out.println(record.get("name")+" "+record.get("centrality"));
        }
    }
}
