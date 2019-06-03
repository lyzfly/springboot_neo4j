package com.lyz.springboot_neo4j.service;

import com.lyz.springboot_neo4j.entity.Person;
import com.lyz.springboot_neo4j.repository.Personrepository;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PersonIml implements PersonService {

    @Autowired
    Personrepository personrepository;
    @Override
    public StatementResult FindImportance() {
        return personrepository.findimportance();

    }

}
