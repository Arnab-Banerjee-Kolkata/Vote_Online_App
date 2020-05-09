package com.example.voteonlinebruh.models;

import java.io.Serializable;

public class PublicCandidate implements Serializable {
    private String name, partyName, symbol;
    private int id;

    public PublicCandidate(int id, String name, String partyName, String symbol) {
        this.name = name;
        this.partyName = partyName;
        this.symbol = symbol;
        this.id = id;
    }

    public String getPartyName() {
        return partyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
