package com.example.voteonlinebruh.models;

import java.io.Serializable;

public class PublicCandidate implements Serializable {
    private String name, partyName, image, symbol, id;

    public PublicCandidate(String id, String name, String partyName, String image, String symbol) {
        this.name = name;
        this.partyName = partyName;
        this.image = image;
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

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }
}
