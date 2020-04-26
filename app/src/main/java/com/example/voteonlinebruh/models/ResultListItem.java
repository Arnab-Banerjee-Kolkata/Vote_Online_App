package com.example.voteonlinebruh.models;

import java.io.Serializable;

public class ResultListItem implements Serializable {
    private int ElectionId, status;
    private String type, name;

    public ResultListItem(int electionId, int status, String type, String name) {
        ElectionId = electionId;
        this.status = status;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getElectionId() {
        return ElectionId;
    }

    public int getStatus() {
        return status;
    }
}