package com.example.voteonlinebruh.activities;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.voteonlinebruh.R;

public class VotingInstructions extends AppCompatActivity implements View.OnClickListener {

    Button proceed, demo;
    String aadhaarNo = "";
    Context mContext;
    int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.TM.getThemeId() == R.style.AppTheme_Light) {
            setTheme(R.style.dTheme_Light);
            themeId = R.style.ConfirmTheme_Light;
        } else {
            setTheme(R.style.dTheme_Dark);
            themeId = R.style.ConfirmTheme_Dark;
        }
        setContentView(R.layout.activity_voting_instructions);
        proceed = findViewById(R.id.proceed);
        demo = findViewById(R.id.demo);
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demo.setEnabled(false);
                Intent intent = new Intent(mContext, Demo.class);
                startActivity(intent);

            }
        });
        mContext = getApplicationContext();
        proceed.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(proceed)) {
            v.setEnabled(false);
            Intent intent = new Intent(mContext, OtpPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            final Bundle bundle=getIntent().getBundleExtra("bundle");
            intent.putExtra("bundle",bundle);
            mContext.startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        demo.setEnabled(true);
        proceed.setEnabled(true);
        super.onResume();
    }
}
