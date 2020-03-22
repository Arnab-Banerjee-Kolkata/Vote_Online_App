package com.example.voteonlinebruh;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

public class ScreenControl
{
    public void makeScreenUnresponsive(Window currentWindow)
    {
        currentWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void makeWindowResponsive(Window currentWindow)
    {
        currentWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
