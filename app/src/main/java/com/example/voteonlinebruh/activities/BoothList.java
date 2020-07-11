package com.example.voteonlinebruh.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.RecyclerViewForBoothListAdapter;
import com.example.voteonlinebruh.models.BoothDetailItem;
import com.example.voteonlinebruh.utility.ThemeManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class BoothList extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_booth_list);
    Toolbar toolbar = findViewById(R.id.toolbarBooth);
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
    @SuppressWarnings("unchecked") final ArrayList<BoothDetailItem> list =
        (ArrayList<BoothDetailItem>) getIntent().getSerializableExtra("list");
    RecyclerViewForBoothListAdapter.NamedLocation[] locations = new RecyclerViewForBoothListAdapter.NamedLocation[list.size()];
    for (int i = 0; i < locations.length; i++) {
      BoothDetailItem item = list.get(i);
      locations[i] =
          new RecyclerViewForBoothListAdapter.NamedLocation(
              item.getArea(), item.getAddress(), new LatLng(item.getLat(), item.getLng()));
    }
    RecyclerView recyclerView = findViewById(R.id.recyclerView2);
    recyclerView.setHasFixedSize(true);
    RecyclerViewForBoothListAdapter adapter =
        new RecyclerViewForBoothListAdapter(locations, getApplicationContext());
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RecyclerViewForBoothListAdapter.OnItemClickListener() {
          @Override
          public void onItemClick(int position) {
            Intent intent = new Intent(getApplicationContext(), BoothDetailsMap.class);
            intent.putExtra("booth",list.get(position));
            intent.putExtra("city",getIntent().getStringExtra("city"));
            startActivity(intent);
          }
        });
  }
}
