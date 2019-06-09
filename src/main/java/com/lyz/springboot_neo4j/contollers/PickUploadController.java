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
    public String uploadFile(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return "上传失败，请选择文件";
        }
        String fileName = file.getOriginalFilename();

        String filePath = "G:/中国电科CETC/springboot_neo4j/src/main/resources/static/";

        File dest = new File(filePath+fileName);
        try {
            file.transferTo(dest);
            upLoadFile.loadcsvToNeo4j();
            return "上传成功！";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }
}
