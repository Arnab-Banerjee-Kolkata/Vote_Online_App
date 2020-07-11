package com.example.voteonlinebruh.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.api.PublicAPICall;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.HashMap;

public class PublicElectionEntryPoint extends AppCompatActivity {

  private Button b;
  private ListView listView;
  private ProgressBar progressBar;
  @SuppressLint("StaticFieldLeak")
  static PublicElectionEntryPoint instance;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_public_election);
    instance = this;
    Toolbar toolbar = findViewById(R.id.toolbarpub);
    listView = findViewById(R.id.list);
    View view = findViewById(R.id.bgContainer);
    if (themeId == R.style.AppTheme_Light) {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
      view.setBackground(getDrawable(android.R.drawable.dialog_holo_light_frame));
    } else {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
      view.setBackground(getDrawable(android.R.drawable.dialog_holo_dark_frame));
    }
    toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
    ImageView imageView1 = findViewById(R.id.homeBg);
    int resid = R.drawable.homebg;
    Glide.with(this).load(resid).into(imageView1);
    b = findViewById(R.id.login);
    b.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            b.setEnabled(false);
            Intent intent = new Intent(getApplicationContext(), VotingInstructions.class);
            startActivity(intent);
          }
        });
    progressBar = findViewById(R.id.cityListLoadProgress);
    PublicAPICall publicAPICall = new PublicAPICall();
    publicAPICall.getBoothCities(getApplicationContext(), PublicElectionEntryPoint.this);
  }

  @SuppressWarnings("ConstantConditions")
  public void release(@Nullable HashMap<String, String> cities) {
    if (null != cities) {
      final Object[] places = cities.keySet().toArray();
      ArrayAdapter<Object> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener(
          new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              listView.setEnabled(false);
              PublicAPICall apiCall = new PublicAPICall();
              apiCall.getBoothLocations(places[position].toString(), getApplicationContext());
              Intent intent = new Intent(getBaseContext(), WaitScreen.class);
              intent.putExtra(
                  "LABEL", "Please wait while we find booths in " + places[position].toString());
              startActivity(intent);
              overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
          });
    }
    progressBar.setVisibility(View.GONE);
  }

  @Override
  protected void onResume() {
    b.setEnabled(true);
    listView.setEnabled(true);
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    instance = null;
    super.onDestroy();
  }
}
