package com.lyz.springboot_neo4j.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloTest {
    @RequestMapping("/HELLO")
    public String hello(){
        return "hello";
    }

}
