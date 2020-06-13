package com.example.voteonlinebruh.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.models.BoothDetailItem;
import com.example.voteonlinebruh.utility.ThemeManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class BoothDetailsMap extends FragmentActivity implements OnMapReadyCallback {

  private Toolbar toolbar;
  private GoogleMap mMap;
  private BoothDetailItem item;
  private TextView city, locality, locality2, landmark, address, coordinates;
  private int themeId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_booth_details_map);
    toolbar = findViewById(R.id.toolbarMap);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            onBackPressed();
          }
        });
    item = (BoothDetailItem) getIntent().getSerializableExtra("booth");
    city = findViewById(R.id.cityName);
    locality = findViewById(R.id.locality);
    locality2 = findViewById(R.id.locality2);
    landmark = findViewById(R.id.landmark);
    address = findViewById(R.id.address);
    coordinates = findViewById(R.id.coordinates);
    city.setText(getIntent().getStringExtra("city"));
    locality.setText(item.getArea());
    locality2.setText("Booth in " + item.getArea());
    landmark.setText(item.getLandmark());
    address.setText(item.getAddress());
    coordinates.setText(item.getLat() + ", " + item.getLng());
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  /**
   * Manipulates the map once available. This callback is triggered when the map is ready to be
   * used. This is where we can add markers or lines, add listeners or move the camera. In this
   * case, we just add a marker near Sydney, Australia. If Google Play services is not installed on
   * the device, the user will be prompted to install it inside the SupportMapFragment. This method
   * will only be triggered once the user has installed Google Play services and returned to the
   * app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    LatLng area = new LatLng(item.getLat(), item.getLng());
    mMap.addMarker(new MarkerOptions().position(area).title("Marker in " + item.getArea()));
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area,16f));
  }
}
