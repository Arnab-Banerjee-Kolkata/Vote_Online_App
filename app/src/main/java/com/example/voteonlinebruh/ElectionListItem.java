package com.example.voteonlinebruh;

import java.io.Serializable;

public class ElectionListItem implements Serializable {
    private String electionId;
    private String state;
    private String type;
    private String phaseCode;
    private String startTime;
    private String endTime;
    private int status;

    ElectionListItem(String electionId, String state, String type, String phaseCode, int status, String startTime, String endTime) {
        this.electionId = electionId;
        this.state = state;
        this.type = type;
        this.phaseCode = phaseCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    String getType() {
        return type;
    }

    String getState() {
        return state;
    }

    String getElectionId() {
        return electionId;
    }

    public int getStatus() {
        return status;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
