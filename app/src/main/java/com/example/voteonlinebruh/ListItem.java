package com.example.voteonlinebruh;

public class ListItem {
    private String name;
    private int state;

    ListItem(String name, int state) {
        this.name = name;
        this.state = state;
    }

    String getName() {
        return name;
    }
    int getState(){
        return state;
    }
}
