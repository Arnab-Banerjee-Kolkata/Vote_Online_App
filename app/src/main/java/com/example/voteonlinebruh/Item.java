package com.example.voteonlinebruh;

public class Item {
    
    private String pname,cname;
    private int indicator;
    private String imgUrl;
    public Item(String imgUrl,String pname,String cname,int indicator)
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
