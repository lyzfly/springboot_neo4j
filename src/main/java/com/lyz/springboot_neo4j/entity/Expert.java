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
    private String similarnodename;
    private String similarnodeorg;
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

    public void

    setOrgnizationname(String orgnizationname) {
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

    public String getSimilarnodename() {
        return similarnodename;
    }

    public void setSimilarnodename(String similarnodename) {
        this.similarnodename = similarnodename;
    }

    public String getSimilarnodeorg() {
        return similarnodeorg;
    }

    public void setSimilarnodeorg(String similarnodeorg) {
        this.similarnodeorg = similarnodeorg;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String toString(){
        return null;
    }
}
    