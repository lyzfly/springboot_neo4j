package com.lyz.springboot_neo4j.contollers;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Controller


    public class AjaxController {

        @RequestMapping(value = "/ajaxtest")
        public String f(){
            return "ajaxtest";
        }

        @ResponseBody
        @RequestMapping(value="/testajax",method=RequestMethod.POST)
        public String testAjax(@RequestParam("num") String num, @RequestParam("age") String age, @RequestParam("peer") String peer) {
            Map<String,String> map=new HashMap<String, String>();
            map.put("num", "AA "+num);
            map.put("age", "BB "+age);
            map.put("peer", "CC "+peer);
             String json = JSON.toJSONString(map);
            return json;
        }
    }

