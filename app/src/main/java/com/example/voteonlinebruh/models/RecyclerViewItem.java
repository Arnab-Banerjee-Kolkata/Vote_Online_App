package com.example.voteonlinebruh.models;

public class RecyclerViewItem {
    
    private String pname,cname;
    private int indicator;
    private String imgUrl;
    public RecyclerViewItem(String imgUrl, String pname, String cname, int indicator)
    {
        this.imgUrl=imgUrl;
        this.cname=cname;
        this.pname=pname;
        this.indicator=indicator;
    }

    public String getImgUrl() {
        return imgUrl;
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
}
