package com.example.voteonlinebruh.models;

import com.example.voteonlinebruh.R;

public class ThemeManager {
    private int themeId;

    public void change(int x) {
        if (x == 0)
            themeId = R.style.AppTheme_Dark;
        else
            themeId = R.style.AppTheme_Light;
    }

    public  int getThemeId() {
        return themeId;
    }
}
