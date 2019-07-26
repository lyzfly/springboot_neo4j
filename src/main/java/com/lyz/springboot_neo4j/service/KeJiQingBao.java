package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhzy on 19-7-17.
 */
@Service
public class KeJiQingBao {

@Autowired Driver driver;

    public void f() {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                String cypher = "match p=(a:EXPERT{id:410})-[:EXPERT_COOPERATE_COUNT*..2]-(b:EXPERT) with p,size(nodes(p)) as totalcount limit 30 return p order by totalcount desc";
                StatementResult result = tx.run(cypher);
                if (result != null) {
                    int cnt = 0;
                    List<Record> records = result.list();
                    for (Record record : records) {
                        cnt++;
                        Path path = record.get("p").asPath();
                        System.out.println(path.relationships().toString());
                        for(Node node:path.nodes()){
                            System.out.print(node.get("name")+" ");
                        }
                        System.out.println();
                        System.out.println(path.start().get("name") + " " + path.end().get("name") + " " + path.end().get("orgnizationname"));
                        System.out.println();
                    }
                    System.out.println(cnt);
                }
            }
        }
    }
}
