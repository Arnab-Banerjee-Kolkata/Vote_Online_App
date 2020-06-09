package com.example.voteonlinebruh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.ListViewForConstituencyDetailsAdapter;
import com.example.voteonlinebruh.models.ConstituencyDetailResult;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;

public class ConstituencyDetailsActivity extends AppCompatActivity {
  private ListView listView;
  private ImageView partySymbol;
  private Toolbar toolbar;
  private int themeId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_constituency_details);
    toolbar = findViewById(R.id.toolbarDetails);
    if (themeId == R.style.AppTheme_Light) {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    } else {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }
    toolbar.setNavigationOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                onBackPressed();
              }
            });
    ArrayList<ConstituencyDetailResult> list =
        (ArrayList<ConstituencyDetailResult>) getIntent().getSerializableExtra("list");
    Log.d("list", list.toString());
    listView = findViewById(R.id.detailResultListView);
    partySymbol = findViewById(R.id.winningPartySym);
    ListViewForConstituencyDetailsAdapter adapter =
        new ListViewForConstituencyDetailsAdapter(list, partySymbol, this.getBaseContext());
    listView.setAdapter(adapter);
    listView.setEnabled(false);
  }
}
