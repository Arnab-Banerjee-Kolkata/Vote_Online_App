package com.example.voteonlinebruh.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

public class VotingInstructions extends AppCompatActivity implements View.OnClickListener {

    private Button proceed, demo;
  private Context mContext;
  @SuppressLint("StaticFieldLeak")
  static VotingInstructions instance;

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        int themeId = ThemeManager.getThemeId();
    if (themeId == R.style.AppTheme_Light) setTheme(R.style.dTheme_Light);
    else setTheme(R.style.dTheme_Dark);
    setContentView(R.layout.activity_voting_instructions);
    instance = this;
        Toolbar toolbar = findViewById(R.id.toolbarIns);
    if (themeId == R.style.AppTheme_Light)
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    else toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
    proceed = findViewById(R.id.proceed);
    demo = findViewById(R.id.demo);
    demo.setOnClickListener(
        new View.OnClickListener() {
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
  public void onClick(View v) {
    if (v.equals(proceed)) {
      v.setEnabled(false);
      Intent intent = new Intent(mContext, OtpPage.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      mContext.startActivity(intent);
    }
  }

  @Override
  protected void onResume() {
    demo.setEnabled(true);
    proceed.setEnabled(true);
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    instance = null;
    super.onDestroy();
  }
}
