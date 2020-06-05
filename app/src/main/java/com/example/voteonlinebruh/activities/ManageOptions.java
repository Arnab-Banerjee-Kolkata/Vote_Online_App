package com.example.voteonlinebruh.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

public class ManageOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeManager.getThemeId());
        setContentView(R.layout.activity_manage_options);
    }
}
