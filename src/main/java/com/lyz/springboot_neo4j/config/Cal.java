package com.lyz.springboot_neo4j.config;

import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class Cal {

    @Autowired
    Neo4jUtil neo4jUtil;

    public void calculate(){

        String query0 = "CALL algo.louvain('EXPERT','rel',{write:true,writeproperty:'community'})" +
                "YIELD nodes,communityCount,iterations,loadMillis,computeMillis,writeMillis";
        neo4jUtil.excuteCypherSql(query0);

        String query1 = "Call algo.labelPropagation('EXPERT','rel',{iterations:3,writeProperty:'partition',write:true,direction:'both'})";
        neo4jUtil.excuteCypherSql(query1);

    }

}
