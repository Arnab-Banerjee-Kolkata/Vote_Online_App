package com.example.voteonlinebruh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;

public class PrivateElection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_private_election);

        Toolbar toolbar=findViewById(R.id.toolbarpriv);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView imageView1 = findViewById(R.id.privBg);
        int resid = R.drawable.vote2hands;
        Glide
                .with(this)
                .load(resid).into(imageView1);
    }
}
