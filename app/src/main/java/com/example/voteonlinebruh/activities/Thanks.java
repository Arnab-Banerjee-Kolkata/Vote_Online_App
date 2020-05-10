package com.example.voteonlinebruh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;

public class Thanks extends AppCompatActivity {

    private int themeId = MainActivity.TM.getThemeId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(themeId);
        setContentView(R.layout.activity_thanks);
        ImageView imageView1 = findViewById(R.id.voted);
        int resid = R.drawable.voted;
        Glide
                .with(this)
                .load(resid).into(imageView1);
    }
}
