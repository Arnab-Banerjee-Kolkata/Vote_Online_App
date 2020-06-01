package com.example.voteonlinebruh.utility;

import com.example.voteonlinebruh.R;

public class ThemeManager {
    private static int themeId;

    public void change(int x) {
        if (x == 0)
            themeId = R.style.AppTheme_Dark;
        else
            themeId = R.style.AppTheme_Light;
    }

    public static int getThemeId() {
        return themeId;
    }
}
