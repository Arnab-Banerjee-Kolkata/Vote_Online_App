package com.example.voteonlinebruh.models;

import com.example.voteonlinebruh.utility.CaseConverter;

import java.io.Serializable;

public class StateListItem implements Serializable {
    private String stateName, stateCode;

    public StateListItem(String stateName, String stateCode) {
        this.stateName = new CaseConverter().toCamelCase(stateName);
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateCode() {
        return stateCode;
    }
}
