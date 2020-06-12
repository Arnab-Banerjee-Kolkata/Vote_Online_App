package com.example.voteonlinebruh.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.HashMap;

public class PublicElectionEntryPoint extends AppCompatActivity {

  private Button b;
  private Toolbar toolbar;
  private ImageView imageView1;
  private ListView listView;
  private View view;
  private ArrayAdapter<Object> adapter;
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
    listView = findViewById(R.id.list);
    view = findViewById(R.id.bgContainer);
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
    imageView1 = findViewById(R.id.homeBg);
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
    HashMap<String,String> cities= (HashMap<String,String>)getIntent().getSerializableExtra("map");
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item,cities.keySet().toArray());
    listView.setAdapter(adapter);
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
