package com.example.voteonlinebruh.models;

import com.example.voteonlinebruh.utility.CaseConverter;

public class RecyclerViewItem {

  private final String partyName, candidateName;
  private final int indicator;
  private final String symbol, image;

  public RecyclerViewItem(String symbol, String image, String partyName, String candidateName, int indicator) {
    this.image = image;
    CaseConverter converter = new CaseConverter();
    this.symbol = symbol;
    this.candidateName = converter.toCamelCase(candidateName);
    this.partyName = partyName;
    this.indicator = indicator;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getPartyName() {
    return partyName;
  }

  public String getCandidateName() {
    return candidateName;
  }

  public int getIndicator() {
    return indicator;
  }

  public String getImage() {
    return image;
  }
}
