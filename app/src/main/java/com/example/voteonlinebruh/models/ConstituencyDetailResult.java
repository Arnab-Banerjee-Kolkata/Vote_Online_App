package com.example.voteonlinebruh.models;

import androidx.annotation.NonNull;

import com.example.voteonlinebruh.utility.CaseConverter;

import java.io.Serializable;

public class ConstituencyDetailResult implements Serializable {
    private final String name;
    private final String image;
    private final String partyName;
    private final String partySymbol;
    private final int votes;

    public ConstituencyDetailResult(String name, String image, String partyName, String partySymbol, int votes) {
        this.name = new CaseConverter().toCamelCase(name);
        this.image = image;
        this.partyName = partyName;
        this.partySymbol = partySymbol;
        this.votes = votes;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPartySymbol() {
        return partySymbol;
    }

    public String getPartyName() {
        return partyName;
    }

    public int getVotes() {
        return votes;
    }

    @NonNull
    @Override
    public String toString() {
        return "result["+name+","+partyName+","+image+","+votes;
    }
}
