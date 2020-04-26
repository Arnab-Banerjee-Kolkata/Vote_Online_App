package com.example.voteonlinebruh.models;

import java.io.Serializable;

public class PartywiseResultList implements Serializable {

    private String partyname, partySymbol;
    private int seatsWon;


    public PartywiseResultList(String partyname, int seatsWon, String partySymbol) {
        this.partyname = partyname;
        this.seatsWon = seatsWon;
        this.partySymbol = partySymbol;
    }

    public int getSeatsWon() {
        return seatsWon;
    }

    public String getPartyname() {
        return partyname;
    }

    public String getPartySymbol() {
        return partySymbol;
    }
}
