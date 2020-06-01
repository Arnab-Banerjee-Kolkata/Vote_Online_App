package com.example.voteonlinebruh.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

public class PublicElectionEntryPoint extends AppCompatActivity {

    private Button b;
    private Toolbar toolbar;
    private ImageView imageView1;
    private int themeId;
    protected static PublicElectionEntryPoint instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeId = ThemeManager.getThemeId();
        setTheme(themeId);
        setContentView(R.layout.activity_public_election);
        instance = this;
        toolbar = findViewById(R.id.toolbarpub);
        if (themeId == R.style.AppTheme_Light)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        else
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imageView1 = findViewById(R.id.homeBg);
        int resid = R.drawable.homebg;
        Glide
                .with(this)
                .load(resid).into(imageView1);
        b = findViewById(R.id.login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), VotingInstructions.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        b.setEnabled(true);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
