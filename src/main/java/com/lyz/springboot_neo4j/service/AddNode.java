package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhzy on 19-9-20.
 */

@Service
public class AddNode {
    @Autowired
    Neo4jUtil neo4jUtil;

    public void AddNode(String cypher){
        neo4jUtil.excuteCypherSql(cypher);
    }
}
