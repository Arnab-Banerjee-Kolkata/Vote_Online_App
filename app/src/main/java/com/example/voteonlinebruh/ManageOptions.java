package com.example.voteonlinebruh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ManageOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_manage_options);
    }
}
