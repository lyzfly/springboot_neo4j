package com.lyz.springboot_neo4j;

import com.lyz.springboot_neo4j.service.DeleteGraph;
import com.lyz.springboot_neo4j.service.GenGraph;
import com.lyz.springboot_neo4j.service.PersonConnectivity;
import com.lyz.springboot_neo4j.service.UpLoadFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootNeo4jApplicationTests {

    @Autowired
    private UpLoadFile upLoadFile;
    @Test
    public void contextLoads() {
        upLoadFile.loadcsvToNeo4j("node.csv", "relation.csv");
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
    public void setGenGraph() throws IOException {
        genGraph.writeToCsv();
    }

    @Autowired
    private PersonConnectivity personConnectivity;
    @Test
    public void connective(){
        personConnectivity.ifConnective("2", "3");
    }
}
