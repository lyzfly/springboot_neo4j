package com.lyz.springboot_neo4j;

import com.lyz.springboot_neo4j.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootNeo4jApplicationTests {

    @Autowired
    private UpLoadFile upLoadFile;
    @Test
    public void contextLoads() {
        String arr[] = {"souce","target"};
        upLoadFile.loadcsvToNeo4j("relation.csv");
    }

    @Autowired
    private DeleteGraph deleteGraph;
    @Test
    public void deletegraph(){
        deleteGraph.delete();
    }


    @Autowired
    private GenGraph genGraph;


    @Test
    public void gencsv() throws IOException{
        genGraph.writeToCsv();
    }
    @Test
    public void setGenGraph() throws IOException {
        genGraph.readitem("relation.csv");
    }


    @Autowired
    private PersonConnectivity personConnectivity;
    @Test
    public void connective(){
        personConnectivity.ifConnective("2", "3");
    }


    @Autowired
    private KeJiQingBao keJiQingBao;
    @Test
    public void setKeJiQingBao(){

        keJiQingBao.f();
    }

    @Autowired
    private  SqlServerService sqlServerService;

    @Test
    public void sqlservice() throws SQLException {
        sqlServerService.find_keywords();
    }

    @Autowired
    private SqlServer2CSV sqlServer2CSV;
    @Test
    public void f() throws IOException {
        sqlServer2CSV.ReadCsv(new File("/home/zhzy/Documents/keywords.csv"),new File("/home/zhzy/Documents/result.csv"));
    }
}
