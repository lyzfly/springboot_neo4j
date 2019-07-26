package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.entity.Expert;
import com.lyz.springboot_neo4j.service.PersonCommunity;
import com.lyz.springboot_neo4j.service.PersonConnectivity;
import com.lyz.springboot_neo4j.service.PersonImportance;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/getOnePersonImportance",method = RequestMethod.POST)
    @ResponseBody
    public String findOneNodeImportance(HttpServletRequest request){

        String expert_name = request.getParameter("expert_name");
        String orgnizationname = request.getParameter("orgnizationname");
        return personImportance.findOnePersonImportance(expert_name,orgnizationname);
    }

    @RequestMapping(value = "/DegreeMostImportantance",method = RequestMethod.POST)
    @ResponseBody
    public List<Expert> DegreeMostImportant(HttpServletRequest request) {
        String orgname = request.getParameter("orgname");
        int cnt = Integer.valueOf(request.getParameter("cnt"));
        request.getParameter("cnt");
        return personImportance.DegreeMostImportant(orgname,cnt);
    }

    @RequestMapping(value = "/PageRankMostImportantance")
    @ResponseBody
    public HashMap<String,Double> PageRankMostImportant(HttpServletRequest request){
        String orgnizationname = request.getParameter("orgnizationname");
        int cnt = Integer.valueOf(request.getParameter("cnt"));
        return personImportance.PageRankMostImportant(orgnizationname,cnt);
    }

    public PersonImportance getPersonImportance() {
        return personImportance;
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
