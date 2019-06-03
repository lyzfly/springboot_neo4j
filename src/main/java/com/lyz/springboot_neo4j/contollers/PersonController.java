package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.service.person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class PersonController {
    @RequestMapping(value = "/FindImportance")
    @ResponseBody
    public HashMap<String,String> FindImportance(){
        return new person().findImportance();
        
    }
}
