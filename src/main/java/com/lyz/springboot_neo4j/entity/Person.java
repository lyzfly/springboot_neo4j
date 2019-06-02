package com.lyz.springboot_neo4j.entity;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "Person")
public class Person {


    @Id@GeneratedValue
    private long nodeId;


    @Property(name = "name")
    private String name;


    public Person(){}

    public Person(String name){
        this.name = name;
    }
    public long getNodeId() {
        return nodeId;
    }

    public void setUuid(long uuid) {
        this.nodeId = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






}
