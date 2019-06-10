package com.lyz.springboot_neo4j.contollers;

import com.lyz.springboot_neo4j.service.UpLoadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/uploadfile")
    @ResponseBody
    public String uploadFile(@RequestParam(value = "file") MultipartFile file,@RequestParam(value = "file1") MultipartFile file1){
        if(file.isEmpty()){
            return "上传失败，请选择文件";
        }
        String nodename = file.getOriginalFilename();

        String path = "G:/neo4j-community-3.5.6/import/";

        String relname = file1.getOriginalFilename();

        File dest_node = new File(path+nodename);

        File dest_rel = new File(path+relname);
        try {
            file.transferTo(dest_node);
            file1.transferTo(dest_rel);
            upLoadFile.loadcsvToNeo4j(nodename,relname);
            return "上传成功！";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }
}
