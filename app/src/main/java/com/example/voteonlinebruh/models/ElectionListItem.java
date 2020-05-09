package com.example.voteonlinebruh.models;

import com.example.voteonlinebruh.utility.CaseConverter;

import java.io.Serializable;

public class ElectionListItem implements Serializable {
    private int electionId;
    private String name;
    private String type;
    private int status;
    private int year;

    public ElectionListItem(int electionId, String name, String type, int status, int year) {
        CaseConverter converter=new CaseConverter();
        this.electionId = electionId;
        this.name = converter.toCamelCase(name);
        this.type = type;
        this.status = status;
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getElectionId() {
        return electionId;
    }

    public int getStatus() {
        return status;
    }

    public int getYear() {
        return year;
    }
}
