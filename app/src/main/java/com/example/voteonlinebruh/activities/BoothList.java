package com.example.voteonlinebruh.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.adapters.RecyclerViewForBoothListAdapter;
import com.example.voteonlinebruh.utility.ThemeManager;
import com.google.android.gms.maps.model.LatLng;

public class BoothList extends AppCompatActivity {

  private Toolbar toolbar;
  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  int themeId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_booth_list);
    toolbar = findViewById(R.id.toolbarBooth);
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
    recyclerView = findViewById(R.id.recyclerView2);
    recyclerView.setHasFixedSize(true);
    RecyclerViewForBoothListAdapter adapter =
        new RecyclerViewForBoothListAdapter(LIST_LOCATIONS, getApplicationContext());
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
  }

  private static final RecyclerViewForBoothListAdapter.NamedLocation[] LIST_LOCATIONS =
      new RecyclerViewForBoothListAdapter.NamedLocation[] {
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Cape Town", new LatLng(-33.920455, 18.466941)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Beijing", new LatLng(39.937795, 116.387224)),
        new RecyclerViewForBoothListAdapter.NamedLocation("Bern", new LatLng(46.948020, 7.448206)),
        new RecyclerViewForBoothListAdapter.NamedLocation("Breda", new LatLng(51.589256, 4.774396)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Brussels", new LatLng(50.854509, 4.376678)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Copenhagen", new LatLng(55.679423, 12.577114)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Hannover", new LatLng(52.372026, 9.735672)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Helsinki", new LatLng(60.169653, 24.939480)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Hong Kong", new LatLng(22.325862, 114.165532)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Istanbul", new LatLng(41.034435, 28.977556)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Johannesburg", new LatLng(-26.202886, 28.039753)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Lisbon", new LatLng(38.707163, -9.135517)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "London", new LatLng(51.500208, -0.126729)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Madrid", new LatLng(40.420006, -3.709924)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Mexico City", new LatLng(19.427050, -99.127571)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Moscow", new LatLng(55.750449, 37.621136)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "New York", new LatLng(40.750580, -73.993584)),
        new RecyclerViewForBoothListAdapter.NamedLocation("Oslo", new LatLng(59.910761, 10.749092)),
        new RecyclerViewForBoothListAdapter.NamedLocation("Paris", new LatLng(48.859972, 2.340260)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Prague", new LatLng(50.087811, 14.420460)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Rio de Janeiro", new LatLng(-22.90187, -43.232437)),
        new RecyclerViewForBoothListAdapter.NamedLocation("Rome", new LatLng(41.889998, 12.500162)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Sao Paolo", new LatLng(-22.863878, -43.244097)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Seoul", new LatLng(37.560908, 126.987705)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Stockholm", new LatLng(59.330650, 18.067360)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Sydney", new LatLng(-33.873651, 151.2068896)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Taipei", new LatLng(25.022112, 121.478019)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Tokyo", new LatLng(35.670267, 139.769955)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Tulsa Oklahoma", new LatLng(36.149777, -95.993398)),
        new RecyclerViewForBoothListAdapter.NamedLocation("Vaduz", new LatLng(47.141076, 9.521482)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Vienna", new LatLng(48.209206, 16.372778)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Warsaw", new LatLng(52.235474, 21.004057)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Wellington", new LatLng(-41.286480, 174.776217)),
        new RecyclerViewForBoothListAdapter.NamedLocation(
            "Winnipeg", new LatLng(49.875832, -97.150726))
      };
}
