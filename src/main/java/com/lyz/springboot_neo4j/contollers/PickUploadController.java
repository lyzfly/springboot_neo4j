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
    @ResponseBody
    @RequestMapping(value = "/uploadfile",method = RequestMethod.POST)

    public String uploadFile(@RequestParam( value = "file") MultipartFile file){
        if(file.isEmpty()){
            return "上传失败，请选择文件";
        }

        String relname = file.getOriginalFilename();

        System.out.println(relname);

        String path = "/bigdata/neo4j-community-3.5.5/import/";

        File dest_rel = new File(path+relname);

        try {
            file.transferTo(dest_rel);
            String arr[] = upLoadFile.readitem(dest_rel);
            upLoadFile.loadcsvToNeo4j(relname);
            return "上传成功！";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }
}
