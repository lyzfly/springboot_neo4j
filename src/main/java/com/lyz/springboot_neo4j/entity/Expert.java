package com.lyz.springboot_neo4j.entity;

/**
 * Created by zhzy on 19-7-25.
 */
public class Expert {

    private String name;
    private String orgnizationname;
    private String  orgnizationid;
    private double importance;
    private String community;
    private String similarnode;
    private String similarity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgnizationname() {
        return orgnizationname;
    }

    public void setOrgnizationname(String orgnizationname) {
        this.orgnizationname = orgnizationname;
    }

    public String getOrgnizationid() {
        return orgnizationid;
    }

    public void setOrgnizationid(String orgnizationid) {
        this.orgnizationid = orgnizationid;
    }

    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getSimilarnode() {
        return similarnode;
    }

    public void setSimilarnode(String similarnode) {
        this.similarnode = similarnode;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }
}
    