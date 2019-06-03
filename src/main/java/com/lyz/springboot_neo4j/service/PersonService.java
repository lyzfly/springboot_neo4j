package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.entity.Person;
import org.neo4j.driver.v1.StatementResult;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PersonService {

    StatementResult FindImportance();

}
