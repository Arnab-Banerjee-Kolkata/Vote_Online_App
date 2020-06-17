package com.example.voteonlinebruh.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class BoothDetailsMap extends FragmentActivity implements OnMapReadyCallback {

  private AppBarLayout appBarLayout;
  private CollapsingToolbarLayout collapsingToolbarLayout;
  private GoogleMap mMap;
  private BoothDetailItem item;
  private TextView city, locality, locality2, landmark, address, coordinates;
  private SupportMapFragment mapFragment;
  private int themeId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_booth_details_map);
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
    appBarLayout = findViewById(R.id.appBarLayout);
    collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout2);
    DisplayMetrics displayMetrics= new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    collapsingToolbarLayout.setMinimumHeight((int) (displayMetrics.heightPixels * .55));
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area, 16f));
    View toolbar =
        ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent())
            .findViewById(Integer.parseInt("4"));

    // and next place it, for example, on bottom right (as Google Maps app)
    RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
    // position on right bottom
    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
    rlp.setMargins(0, 0, 30, 30);
  }

  public void onBackPressed(View view) {
    super.onBackPressed();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (appBarLayout.getLayoutParams() != null) {
      CoordinatorLayout.LayoutParams layoutParams =
          (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
      AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
      appBarLayoutBehaviour.setDragCallback(
          new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
              return false;
            }
          });
      layoutParams.setBehavior(appBarLayoutBehaviour);
    }
  }
}
