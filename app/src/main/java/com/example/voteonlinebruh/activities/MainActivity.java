package com.example.voteonlinebruh.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.api.PublicAPICall;
import com.example.voteonlinebruh.utility.ThemeManager;

public class MainActivity extends AppCompatActivity {

  private static boolean scheduledRestart = false;
  private ThemeManager TM;
  @SuppressLint("StaticFieldLeak")
  public static WebView webView;
  private ConstraintLayout pri, man, pub, res;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final SharedPreferences pref = getSharedPreferences("Voter.Online.Theme", MODE_PRIVATE);
    final SharedPreferences.Editor edit = pref.edit();
    TM = new ThemeManager();
    if (!pref.contains("themeLight")) {
      edit.putBoolean("themeLight", true);
      TM.change(1);
      edit.apply();
    } else {
      if (pref.getBoolean("themeLight", true)) TM.change(1);
      else TM.change(0);
    }
    setTheme(ThemeManager.getThemeId());
    setContentView(R.layout.activity_main);
    webView = findViewById(R.id.wv2);
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
    ImageButton button = findViewById(R.id.themeToggle);
    if (ThemeManager.getThemeId() == R.style.AppTheme_Light) {
      button.setImageDrawable(getDrawable(R.drawable.ic_moon_black_24dp));
      toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
    } else {
      button.setImageDrawable(getDrawable(R.drawable.ic_wb_sunny_black_24dp));
      toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
    }

    button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (ThemeManager.getThemeId() == R.style.AppTheme_Light) {
              TM.change(0);
              edit.putBoolean("themeLight", false);
              scheduledRestart = true;
            } else {
              TM.change(1);
              edit.putBoolean("themeLight", true);
              scheduledRestart = true;
            }
            edit.apply();
            onResume();
          }
        });

    pub = findViewById(R.id.public_button);
    pri = findViewById(R.id.private_button);
    man = findViewById(R.id.manage);
    res = findViewById(R.id.result);
    pub.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            pub.setEnabled(false);
            Intent intent = new Intent(getApplicationContext(), PublicElectionEntryPoint.class);
            startActivity(intent);
          }
        });

    pri.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            pri.setEnabled(false);
            Intent intent = new Intent(getApplicationContext(), PrivateElectionEntryPoint.class);
            startActivity(intent);
          }
        });
    final SharedPreferences preferences = getSharedPreferences("Vote.Online.Account", MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    if (!preferences.contains("loginStatus")) editor.putBoolean("loginStatus", false);
    editor.apply();
    man.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            man.setEnabled(false);
            if (preferences.getBoolean("loginStatus", false)) {
              Intent intent = new Intent(getApplicationContext(), ManageOptions.class);
              intent.putExtra("email", preferences.getString("email", ""));
              startActivity(intent);
            } else {
              Intent intent = new Intent(getApplicationContext(), PrivateElectionManager.class);
              startActivity(intent);
            }
          }
        });

    res.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            res.setEnabled(false);
            PublicAPICall publicAPICall = new PublicAPICall();
            publicAPICall.getPublicResultList(getApplicationContext());
            Intent intent = new Intent(getBaseContext(), WaitScreen.class);
            intent.putExtra("LABEL", "Refreshing");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
          }
        });

    /*UI CODE END*/
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (scheduledRestart) {
      scheduledRestart = false;
      Intent i = new Intent(getBaseContext(), MainActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      finish();
      startActivity(i);
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    pub.setEnabled(true);
    pri.setEnabled(true);
    man.setEnabled(true);
    res.setEnabled(true);
  }

  @Override
  protected void onDestroy() {
    webView = null;
    super.onDestroy();
  }
}
