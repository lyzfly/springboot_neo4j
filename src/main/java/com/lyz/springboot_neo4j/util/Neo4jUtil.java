package com.lyz.springboot_neo4j.util;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.driver.v1.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Neo4jUtil{

    @Autowired
    Driver neo4jDriver;
    public StatementResult excuteCypherSql(String cypherSql) {
        StatementResult result = null;
        try (Session session = neo4jDriver.session()) {
            result = session.run(cypherSql);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    /**
     *
     * @param
     * @return
     */

    public HashMap<String, Object> GetGraphNodeAndShip(StatementResult result) {
        HashMap<String, Object> mo = new HashMap<String, Object>();
        try {
            if (result.hasNext()) {
                List<Record> records = result.list();
                List<HashMap<String, Object>> ents = new ArrayList<HashMap<String, Object>>();
                List<HashMap<String, Object>> ships = new ArrayList<HashMap<String, Object>>();
                List<String> uuids = new ArrayList<String>();
                List<String> shipids = new ArrayList<String>();
                for (Record recordItem : records) {
                    List<Pair<String, Value>> f = recordItem.fields();
                    for (Pair<String, Value> pair : f) {
                        HashMap<String, Object> rships = new HashMap<String, Object>();
                        HashMap<String, Object> rss = new HashMap<String, Object>();
                        String typeName = pair.value().type().name();
                        if (typeName.equals("NULL")) {
                            continue;
                        } else if (typeName.equals("NODE")) {
                            Node noe4jNode = pair.value().asNode();
                            Map<String, Object> map = noe4jNode.asMap();
                            String uuid = String.valueOf(noe4jNode.id());
                            if (!uuids.contains(uuid)) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    rss.put(key, entry.getValue());
                                }
                                rss.put("id", uuid);
                                uuids.add(uuid);
                            }
                        } else if (typeName.equals("RELATIONSHIP")) {
                            Relationship rship = pair.value().asRelationship();
                            String uuid = String.valueOf(rship.id());
                            if (!shipids.contains(uuid)) {
                                String sourceid = String.valueOf(rship.startNodeId());
                                String targetid = String.valueOf(rship.endNodeId());
                                Map<String, Object> map = rship.asMap();
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    rships.put(key, entry.getValue());
                                }
                                rships.put("id", uuid);
                                rships.put("source", sourceid);
                                rships.put("target", targetid);
                            }
                        }else {
                            rss.put(pair.key(), pair.value().toString());
                        }
                        if (rss != null && !rss.isEmpty()) {
                            ents.add(rss);
                        }
                        if (rships != null && !rships.isEmpty()) {
                            ships.add(rships);
                        }
                    }
                }
                mo.put("nodes", ents);
                mo.put("links", ships);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mo;
    }
}
