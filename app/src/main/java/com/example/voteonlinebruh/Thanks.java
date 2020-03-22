package com.example.voteonlinebruh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Thanks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_thanks);
        ImageView imageView1 = findViewById(R.id.voted);
        int resid = R.drawable.voted;
        Glide
                .with(this)
                .load(resid).into(imageView1);
    }
}
