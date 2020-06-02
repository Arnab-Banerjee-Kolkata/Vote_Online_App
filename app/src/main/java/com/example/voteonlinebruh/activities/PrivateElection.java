package com.example.voteonlinebruh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

public class PrivateElection extends AppCompatActivity {

  private ImageView imageView1;
  private Toolbar toolbar;
  private int themeId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_private_election);
    toolbar = findViewById(R.id.toolbarpriv);
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
    imageView1 = findViewById(R.id.privBg);
    int resid = R.drawable.vote2hands;
    Glide.with(this).load(resid).into(imageView1);
  }
}
