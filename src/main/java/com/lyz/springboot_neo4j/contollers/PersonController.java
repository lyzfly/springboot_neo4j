package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.service.person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import org.springframework.web.servlet.ModelAndView;
@Controller

public class PersonController {
    @RequestMapping(value = "/")
    public String index(){
        return "index";
    }
    @RequestMapping(value = "/FindImportance")
    public HashMap<String,String> FindImportance(){
        return new person().findImportance();
        
    }
}
