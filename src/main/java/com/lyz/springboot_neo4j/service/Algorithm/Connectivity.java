package com.lyz.springboot_neo4j.service.Algorithm;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * 判断两个节点是否连通
 */
public class Connectivity extends CallAlgorithm{

    public static boolean shortestPath(Driver driver, String startnode, String  endnode) {
        
        boolean flag = false;
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {

                StatementResult result = tx.run("MATCH (start:Person{name:{x}}), (end:Person{name:{y}})\n" +
                        "CALL algo.shortestPath.stream(start, end,'cost')\n" +
                        "YIELD nodeId, cost\n" +
                        "RETURN algo.asNode(nodeId).name AS name, cost",parameters("x",startnode,"y",endnode));
                while(result.hasNext()) {
                    Record record = result.next();
                    System.out.println(record.get("name")+" "+record.get("cost"));
                    flag = true;
                }
                tx.success();
            }
        }
        System.out.println(flag);
        return flag;
    }

    public static boolean SingleSourceShortest(Driver driver, String startnode) {
        boolean flag = false;
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result  = tx.run("MATCH (n:Loc {name:{x})\n" +
                        "CALL algo.shortestPath.deltaStepping.stream(n, 'cost', 3.0)\n" +
                        "YIELD nodeId, distance\n" +
                        "\n" +
                        "RETURN algo.asNode(nodeId).name AS destination, distance",parameters("x",startnode));
                while(result.hasNext()){
                    Record record = result.next();
                    System.out.println(record.get("destination")+" "+record.get("distance"));
                    flag = true;
                }
                tx.success();
            }
        }
        return flag;
    }
}
