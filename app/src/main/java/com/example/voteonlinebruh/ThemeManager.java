package com.example.voteonlinebruh;

public class ThemeManager {
    private int themeId;

    void change(int x) {
        if (x == 0)
            themeId = R.style.AppTheme_Dark;
        else
            themeId = R.style.AppTheme_Light;
    }

    int getThemeId() {
        return themeId;
    }
}
