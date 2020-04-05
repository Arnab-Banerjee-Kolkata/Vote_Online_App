package com.example.voteonlinebruh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PublicElection extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(MainActivity.TM.getThemeId());
        setContentView(R.layout.activity_public_election);

        //SELECTED OPTION IS FOUND HERE
        String ELECTION_NAME=getIntent().getStringExtra("NAME");

        Toolbar toolbar=findViewById(R.id.toolbarpub);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle(ELECTION_NAME);
        ImageView imageView1 = findViewById(R.id.homeBg);
        int resid = R.drawable.homebg;
        Glide
                .with(this)
                .load(resid).into(imageView1);
        b =findViewById(R.id.login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setEnabled(false);
                Intent intent=new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        b.setEnabled(true);
        super.onResume();
    }
}
