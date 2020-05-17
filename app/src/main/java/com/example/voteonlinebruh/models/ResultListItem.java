package com.example.voteonlinebruh.models;

import com.example.voteonlinebruh.utility.CaseConverter;

import java.io.Serializable;

public class ResultListItem implements Serializable {
    private int electionId, status, year;
    private String type, name, stateName;

    public ResultListItem(int electionId, int status, int year, String type, String name, String stateName) {
        this.year = year;
        this.stateName = stateName;
        CaseConverter converter = new CaseConverter();
        this.electionId = electionId;
        this.status = status;
        this.type = type;
        this.name = converter.toCamelCase(name);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
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

    public String getStateName() {
        return stateName;
    }
}