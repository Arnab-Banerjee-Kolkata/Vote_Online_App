package com.example.voteonlinebruh.models;

public class PublicCandidate
{
    private String name, partyName, symbol;

    public PublicCandidate(String name, String partyName, String symbol)
    {
        this.name=name;
        this.partyName=partyName;
        this.symbol=symbol;
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
}
