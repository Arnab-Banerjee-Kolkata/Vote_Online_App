package com.example.voteonlinebruh.models;

import java.io.Serializable;

public class PartywiseResultList implements Serializable {

  private final String partyname, partySymbol, alliance;
  private final int seatsWon;

  public PartywiseResultList(String partyname, int seatsWon, String partySymbol, String alliance) {
    this.partyname = partyname;
    this.seatsWon = seatsWon;
    this.partySymbol = partySymbol;
    this.alliance = alliance;
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

  public String getAlliance() {
    return alliance;
  }
}
