package com.lyz.springboot_neo4j.service;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GenGraph {
    @Autowired Driver driver;
    public File writeToCsv() throws  IOException{

        File file = new File("relation.csv");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            List<Integer> list = new ArrayList<Integer>();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 20000; i++) {
                int base = 1;
                Random rand = new Random();
                int add_parm1 = rand.nextInt(10000);
                int add_parm2 = rand.nextInt(10000);

                int  personA = base + add_parm1;
                int  personB = base + add_parm2;
                String line = personA + "," + personB;
                sb.append(line+"\r\n");
            }
            outputStream.write(sb.toString().getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void readitem(String filename) throws IOException {
        try (FileReader fr = new FileReader(new File(filename))) {
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String arr[] = line.split(",");
            for(String a:arr){
                System.out.println(a);
            }
        }
    }


   /* public static void gengraph(Driver driver) throws IOException {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                writeToCsv();
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line = "";
                tx.run("LOAD CSV WITH FROM data.csv AS line RETURN count(*)")
                while ((line = br.readLine()) != null) {
                    String[] arr = line.split(" ");
                    StatementResult result = tx.run("MATCH (n:Person) WHERE n.name=$person RETURN n",parameters("person",arr[0]));
                    if (!result.hasNext()) {
                        tx.run("create(a:Person{name:{name}})", parameters("name", arr[0]));
                    }
                    StatementResult result1 = tx.run("MATCH (n:Person) WHERE n.name={x} RETURN n", parameters("x", arr[1]));
                    if (!result1.hasNext()) {
                        tx.run("create(a:Person{name:{name}})", parameters("name", arr[1]));
                    }

                    if(!arr[0].equals(arr[1])) {
                        StatementResult result2 = tx.run("MATCH(a:Person) --> (b:Person) WHERE a.name={x} AND b.name={y} RETURN a",
                                parameters("x", arr[0], "y", arr[1]));
                        if (!result2.hasNext()) {
                            tx.run("MATCH(a:Person),(b:Person) WHERE a.name={x} AND b.name={y} create(a)-[r:Follow]->(b) RETURN r",
                                    parameters("x", arr[0], "y", arr[1]));
                        }
                    }
                }
                tx.success();
            }
        }
    }*/
}
