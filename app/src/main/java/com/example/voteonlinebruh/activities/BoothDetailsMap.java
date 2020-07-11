package com.example.voteonlinebruh.activities;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

@SuppressWarnings("ConstantConditions")
public class BoothDetailsMap extends FragmentActivity implements OnMapReadyCallback {

  private AppBarLayout appBarLayout;
  private CollapsingToolbarLayout collapsingToolbarLayout;
  private BoothDetailItem item;
  private SupportMapFragment mapFragment;

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int themeId = ThemeManager.getThemeId();
    setTheme(themeId);
    setContentView(R.layout.activity_booth_details_map);
    item = (BoothDetailItem) getIntent().getSerializableExtra("booth");
    TextView city = findViewById(R.id.cityName);
    TextView locality = findViewById(R.id.locality);
    TextView locality2 = findViewById(R.id.locality2);
    TextView landmark = findViewById(R.id.landmark);
    TextView address = findViewById(R.id.address);
    TextView coordinates = findViewById(R.id.coordinates);
    city.setText(getIntent().getStringExtra("city"));
    locality.setText(item.getArea());
    locality2.setText("Booth in " + item.getArea());
    landmark.setText(item.getLandmark());
    address.setText(item.getAddress());
    coordinates.setText(item.getLat() + ", " + item.getLng());
    appBarLayout = findViewById(R.id.appBarLayout);
    collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout2);
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
    LatLng area = new LatLng(item.getLat(), item.getLng());
    googleMap.addMarker(new MarkerOptions().position(area).title("Marker in " + item.getArea()));
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area, 16f));
    View toolbar =
        ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent())
            .findViewById(Integer.parseInt("4"));
    // and next place it, for example, on bottom right (as Google Maps app)
    RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
    // position on right bottom
    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
    rlp.setMargins(0, 0, 50, 90);
  }

  @SuppressWarnings("unused")
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
      DisplayMetrics displayMetrics= new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      collapsingToolbarLayout.setMinimumHeight((int) (displayMetrics.heightPixels * .55));
    }
  }
}
