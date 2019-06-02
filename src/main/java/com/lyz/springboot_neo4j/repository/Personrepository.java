package com.lyz.springboot_neo4j.repository;

import com.lyz.springboot_neo4j.entity.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Personrepository extends Neo4jRepository<Person, Long> {

    //根据节点名查询，返回被查询人的所有信息
    Person findByName(String name);

    //查询节点的所有直系亲戚
    @Query("MATCH(p1:Person{name:{name}})-[r:Follow]->(p2:Person) Return p2")
    List<Person> findCloseRelativeByName(@Param("name") String name);



}
