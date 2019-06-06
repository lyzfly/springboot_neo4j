package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.service.PersonImportance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
@Controller

public class PersonController {
    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/importance")
    public String  importance(){
        return "importance";
    }

    @RequestMapping(value = "/community")
    public String community(){
        return "community";
    }


    @Autowired PersonImportance personImportance;
    @RequestMapping(value = "/FindImportance")
    @ResponseBody
    public HashMap<String,String> FindImportance(HttpRequest request){
        return personImportance.findImportance();
    }

    @RequestMapping(value = "/getOnePersonImportance",method = RequestMethod.POST)
    @ResponseBody
    public double findOneNodeImportance(HttpServletRequest request){

        String person_name = request.getParameter("person_name");
        return personImportance.findOnePersonImportance(person_name);
    }

    @RequestMapping(value = "/FindTheMostImport")
    @ResponseBody
    public HashMap<String,String> FindTheMostImportant(){
        return personImportance.findTheMostImport();
    }
    

}
