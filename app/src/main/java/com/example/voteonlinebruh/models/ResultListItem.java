package com.example.voteonlinebruh.models;

import com.example.voteonlinebruh.utility.CaseConverter;

import java.io.Serializable;

public class ResultListItem implements Serializable {
  private final int electionId;
    private final int status;
    private final int year;
  private final String type;
    private final String name;
    private final String stateName;

  public ResultListItem(
      int electionId, int status, int year, String type, String name, String stateName) {
    this.year = year;
    CaseConverter converter = new CaseConverter();
    this.electionId = electionId;
    this.status = status;
    this.type = type;
    this.name = converter.toCamelCase(name);
    this.stateName = converter.toCamelCase(stateName);
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public int getElectionId() {
    return electionId;
  }

  public int getStatus() {
    return status;
  }

  public int getYear() {
    return year;
  }

  public String getStateName() {
    return stateName;
  }
}
