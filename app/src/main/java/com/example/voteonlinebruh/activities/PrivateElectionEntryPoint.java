package com.example.voteonlinebruh.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

public class PrivateElectionEntryPoint extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_private_election_entry_point);
    Toolbar toolbar = findViewById(R.id.toolbarpriv);
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
    ImageView imageView1 = findViewById(R.id.privBg);
    int resid = R.drawable.vote2hands;
    Glide.with(this).load(resid).into(imageView1);
  }
}
