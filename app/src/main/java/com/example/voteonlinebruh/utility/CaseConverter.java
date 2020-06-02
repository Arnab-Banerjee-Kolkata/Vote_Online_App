package com.example.voteonlinebruh.utility;

public class CaseConverter {
  public String toCamelCase(String s) {
    StringBuilder camelString = new StringBuilder();
    s = s.toLowerCase();
    if (s.length() > 0)
      for (String i : s.split(" ")) {
        camelString.append(Character.toUpperCase(i.charAt(0)));
        camelString.append(i.substring(1)).append(" ");
      }
    return camelString.toString();
  }
}
