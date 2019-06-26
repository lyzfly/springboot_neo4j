package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.service.PersonCommunity;
import com.lyz.springboot_neo4j.service.PersonConnectivity;
import com.lyz.springboot_neo4j.service.PersonImportance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller

public class PersonController {

    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }
    @RequestMapping(value = "/index1")
    public String index1(){return "d3";}

    @RequestMapping(value = "/importance")
    public String  importance(){
        return "importance";
    }

    @RequestMapping(value = "/community")
    public String community(){
        return "community";
    }

    @RequestMapping(value = "/connectivity")
    public String connectivity(){
        return "connectivity";
    }

    @Autowired PersonImportance personImportance;
    @RequestMapping(value = "/FindImportance")
    @ResponseBody
    public HashMap<String,String> FindImportance(HttpRequest request){
        return personImportance.findImportance();
    }

    @RequestMapping(value = "/getOnePersonImportance",method = RequestMethod.POST)
    @ResponseBody
    public String findOneNodeImportance(HttpServletRequest request){

        String person_name = request.getParameter("person_name");
        return personImportance.findOnePersonImportance(person_name);
    }

    @RequestMapping(value = "/FindTheMostImport")
    @ResponseBody
    public HashMap<String,Double> FindTheMostImportant() {
        return personImportance.findTheMostImport();
    }

    @Autowired
    PersonConnectivity connectivity;
    @PostMapping("/ifConnnective")
    @ResponseBody
    public List<String> IFconnective(HttpServletRequest request){
        String startnode = request.getParameter("startnode");
        String endnode = request.getParameter("endnode");
        return connectivity.ifConnective(startnode, endnode);
    }

    @Autowired
    PersonCommunity personCommunity;
    @RequestMapping(value = "/getallcommunity",method =RequestMethod.GET)
    @ResponseBody
    public String getallCommunity(){
        return personCommunity.findOnePersonClass();
    }
}
