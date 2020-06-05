package com.example.voteonlinebruh.models;

import com.example.voteonlinebruh.utility.CaseConverter;

public class RecyclerViewItem {

    private String pname, cname;
    private int indicator;
    private String symbol, image;

    public RecyclerViewItem(String symbol, String image, String pname, String cname, int indicator) {
        this.image = image;
        CaseConverter converter = new CaseConverter();
        this.symbol = symbol;
        this.cname = converter.toCamelCase(cname);
        this.pname = pname;
        this.indicator = indicator;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPname() {
        return pname;
    }

    public String getCname() {
        return cname;
    }

    public int getIndicator() {
        return indicator;
    }

    public String getImage() {
        return image;
    }
}
