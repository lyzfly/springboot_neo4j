package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.service.UpLoadFile;
import com.lyz.springboot_neo4j.util.Neo4jUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


@Controller
public class PickUploadController {

    private  static Logger logger = Logger.getLogger(PickUploadController.class);
    @RequestMapping(value = "/upload")
    public String upload(){
        return "upload";
    }

    @Autowired
    UpLoadFile upLoadFile;
    @Autowired
    Neo4jUtil neo4jUtil;
    @ResponseBody
    @RequestMapping(value = "/upload_nodefile",method = RequestMethod.POST)
    public String uploadNode(@RequestParam( value = "nodefile") MultipartFile file) {
        String filename = file.getOriginalFilename();
        String projectpath = System.getProperty("user.dir");
        String path = "/var/lib/neo4j/import/"+filename;
        //String path = "/bigdata/neo4j/neo4j-community-3.5.7/import/"+filename;
        File dest_rel = new File(path);
        try {
            file.transferTo(dest_rel);
            String filepath = "file:///" + path;
            filepath = filepath.replace("\\", "/");
            upLoadFile.loadcsvToNeo4j_node(filepath);

            String query0 = "CALL algo.louvain('EXPERT','rel',{write:true,writeproperty:'community'})" +
                    "YIELD nodes,communityCount,iterations,loadMillis,computeMillis,writeMillis";
            neo4jUtil.excuteCypherSql(query0);

            String query1 = "Call algo.labelPropagation('EXPERT','rel',{iterations:3,writeProperty:'partition',write:true,direction:'both'})";
            neo4jUtil.excuteCypherSql(query1);

            return "添加成功！";
        } catch (IOException e) {
            e.printStackTrace();
            return "添加失败！";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/upload_relfile",method = RequestMethod.POST)
    public String uploadFile1(@RequestParam( value = "relfile") MultipartFile file){
        String filename = file.getOriginalFilename();
        //String projectpath = System.getProperty("user.dir");

        String projectpath = "/var/lib/neo4j/import/";
        //String projectpath = "/bigdata/neo4j/neo4j-community-3.5.7/import/";
        String path = projectpath+filename;
        File dest_rel = new File(path);
        System.out.println(dest_rel.getPath());
        try {
            file.transferTo(dest_rel);
            String filepath = "file:///"+path;
            filepath = filepath.replace("\\", "/");
            System.out.println(filepath);
            upLoadFile.loadcsvToNeo4j_rel(filepath);
            String query0 = "CALL algo.louvain('EXPERT','rel',{write:true,writeproperty:'community'})" +
                    "YIELD nodes,communityCount,iterations,loadMillis,computeMillis,writeMillis";
            neo4jUtil.excuteCypherSql(query0);

            String query1 = "Call algo.labelPropagation('EXPERT','rel',{iterations:3,writeProperty:'partition',write:true,direction:'both'})";
            neo4jUtil.excuteCypherSql(query1);
            return "添加成功！";
        } catch (IOException e) {
            e.printStackTrace();
            return "添加失败！";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/delete_node",method = RequestMethod.POST)
    public String delete_node(HttpServletRequest request){
        String expert_arrstring = request.getParameter("node");
        String[]  arr = expert_arrstring.split(",");
        String query0 = "CALL algo.louvain('EXPERT','rel',{write:true,writeproperty:'community'})" +
                "YIELD nodes,communityCount,iterations,loadMillis,computeMillis,writeMillis";
        neo4jUtil.excuteCypherSql(query0);

        String query1 = "Call algo.labelPropagation('EXPERT','rel',{iterations:3,writeProperty:'partition',write:true,direction:'both'})";
        neo4jUtil.excuteCypherSql(query1);
        return upLoadFile.delete_node(arr);
    }

    @ResponseBody
    @RequestMapping(value = "/delete_edge",method = RequestMethod.POST)
    public String delete_edge(HttpServletRequest request){
        String edge = request.getParameter("edge");
        String startnodeid = edge.split("-")[0];
        String endnodeid = edge.split("-")[1];
        String query0 = "CALL algo.louvain('EXPERT','rel',{write:true,writeproperty:'community'})" +
                "YIELD nodes,communityCount,iterations,loadMillis,computeMillis,writeMillis";
        neo4jUtil.excuteCypherSql(query0);

        String query1 = "Call algo.labelPropagation('EXPERT','rel',{iterations:3,writeProperty:'partition',write:true,direction:'both'})";
        neo4jUtil.excuteCypherSql(query1);
        return upLoadFile.delete_edge(startnodeid,endnodeid);
    }

}
