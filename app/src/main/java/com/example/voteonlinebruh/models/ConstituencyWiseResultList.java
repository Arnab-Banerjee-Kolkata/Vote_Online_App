package com.example.voteonlinebruh.models;

import java.io.Serializable;

public class ConstituencyWiseResultList implements Serializable {
    private String constituencyName, candidateName, partyName, partySymbol;
    private int voteCount;

    public ConstituencyWiseResultList(String constituencyName, String candidateName, String partyName, String partySymbol, int voteCount) {
        this.constituencyName = constituencyName;
        this.candidateName = candidateName;
        this.partyName = partyName;
        this.partySymbol = partySymbol;
        this.voteCount = voteCount;
    }

    public String getConstituencyName() {
        return constituencyName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public String getPartyName() {
        return partyName;
    }

    public String getPartySymbol() {
        return partySymbol;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
