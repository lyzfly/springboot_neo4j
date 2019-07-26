package com.lyz.springboot_neo4j.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class SqlServer2CSV {

    public void ReadCsv(File inputfile,File outputFile) throws IOException {
        FileReader fr = new FileReader(inputfile);
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(outputFile);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("start"+","+"end");
        bw.newLine();
        StringBuffer sb = new StringBuffer();
        String line  = null;
        while((line=br.readLine())!=null){
            line = line.replace('"',' ');
            String Node[] = line.split("\\|");
            for(int i=0;i<Node.length-1;i++) {
                for (int j = i+1; j < Node.length; j++) {
                    String words = Node[i]+","+Node[j];
                    bw.write(words);
                    bw.newLine();
                }
            }
        }
        bw.flush();
        bw.close();
        System.out.println("执行完毕！");
    }
}
