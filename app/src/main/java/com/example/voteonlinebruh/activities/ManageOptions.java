package com.example.voteonlinebruh.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

public class ManageOptions extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(ThemeManager.getThemeId());
    setContentView(R.layout.activity_manage_options);
    String email=getIntent().getStringExtra("email");
    Log.d("email",email);
  }
}
