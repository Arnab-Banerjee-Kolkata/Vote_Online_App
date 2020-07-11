package com.example.voteonlinebruh.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.RecyclerViewForConstituencyDetailsAdapter;
import com.example.voteonlinebruh.models.ConstituencyDetailResult;
import com.example.voteonlinebruh.utility.ThemeManager;

import java.util.ArrayList;

public class ConstituencyDetailsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_constituency_details);
    Toolbar toolbar = findViewById(R.id.toolbarDetails);
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
    @SuppressWarnings("unchecked") ArrayList<ConstituencyDetailResult> list =
        (ArrayList<ConstituencyDetailResult>) getIntent().getSerializableExtra("list");
    boolean tie = false;
    if (list.get(0).getVotes() == list.get(1).getVotes()){
      tie = true;
    }
    else
    {
      ImageView background = findViewById(R.id.imageViewCollapsing2);
      background.setImageResource(R.drawable.winner_bg);
    }
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    ImageView partySymbol = findViewById(R.id.winningPartySym);
    RecyclerViewForConstituencyDetailsAdapter adapter =
        new RecyclerViewForConstituencyDetailsAdapter(
            list, partySymbol,tie, this.getApplicationContext());
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
  }
}
