package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.service.UpLoadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


@Controller
public class PickUploadController {


    @RequestMapping(value = "/upload")
    public String upload(){
        return "upload";
    }

    @Autowired
    UpLoadFile upLoadFile;
    @ResponseBody
    @RequestMapping(value = "/upload_nodefile",method = RequestMethod.POST)
    public String uploadNode(@RequestParam( value = "nodefile") MultipartFile file) {
        String filename = file.getOriginalFilename();
        String path = "/bigdata/neo4j-community-3.5.7/import/";
        File dest_rel = new File(path + filename);
        try {
            file.transferTo(dest_rel);
            upLoadFile.loadcsvToNeo4j_node(filename);
            return "添加成功！";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "添加失败！";
    }

    @ResponseBody
    @RequestMapping(value = "/upload_relfile",method = RequestMethod.POST)
    public String uploadFile1(@RequestParam( value = "relfile") MultipartFile file){
        String filename = file.getOriginalFilename();
        String path = "/bigdata/neo4j-community-3.5.7/import/";
        File dest_rel = new File(path + filename);
        System.out.println(dest_rel.getPath());
        try {
            file.transferTo(dest_rel);
            upLoadFile.loadcsvToNeo4j_rel(filename);
            return "添加成功！";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "添加失败！";
    }

    @ResponseBody
    @RequestMapping(value = "/delete_node",method = RequestMethod.POST)
    public void delete_node(HttpServletRequest request){
        String nodeid = request.getParameter("node");
        upLoadFile.delete_node(nodeid);
    }

    @ResponseBody
    @RequestMapping(value = "/delete_edge",method = RequestMethod.POST)
    public void delete_edge(HttpServletRequest request){
        String edge = request.getParameter("edge");
        String startnodeid = edge.split("-")[0];
        String endnodeid = edge.split("-")[1];
        upLoadFile.delete_edge(startnodeid,endnodeid);
    }

}
