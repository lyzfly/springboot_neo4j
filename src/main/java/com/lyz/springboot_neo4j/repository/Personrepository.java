package com.lyz.springboot_neo4j.repository;

import com.lyz.springboot_neo4j.entity.Person;
import org.neo4j.driver.v1.*;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
@Description:构建Person数据仓库
@author lyz
@date 19-6-3 上午10:28
*/
@Repository
public interface Personrepository extends Neo4jRepository<Person, Long> {

    //查询节点的所有直系亲戚
    @Query("CALL algo.degree.stream(\"Person\",\"In\",{direction:\"incoming\"})"+
            "YIELD nodeId,score RETURN algo.asNode(nodeId).name as name,score as score ORDER BY score DESC LIMIT 1")
    StatementResult findimportance();


    //List<Person> findCloseRelativeByName(@Param("name") String name);


}
