package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.entity.Person;
import com.lyz.springboot_neo4j.repository.Personrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class PersonControler {
    @Autowired
    private Personrepository personrepository;
    @RequestMapping(value = "/person/{name}")
    @ResponseBody
    public Person findbyName(@PathVariable("name") String name){
        return personrepository.findByName(name);
    }
}
