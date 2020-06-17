package com.example.voteonlinebruh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voteonlinebruh.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RecyclerViewForBoothListAdapter
    extends RecyclerView.Adapter<RecyclerViewForBoothListAdapter.ViewHolder> {

  private NamedLocation[] namedLocations;
  private Context context;
  private RecyclerViewForBoothListAdapter.OnItemClickListener mListener;

  public RecyclerViewForBoothListAdapter(NamedLocation[] locations, Context context) {
    super();
    namedLocations = locations;
    this.context = context;
  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public void setOnItemClickListener(RecyclerViewForBoothListAdapter.OnItemClickListener listener) {
    mListener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.booth_list_row, parent, false),
        mListener);
  }
  /**
   * This function is called when the user scrolls through the screen and a new item needs to be
   * shown. So we will need to bind the holder with the details of the next item.
   */
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if (holder == null) {
      return;
    }
    holder.bindView(position);
  }

  @Override
  public int getItemCount() {
    return namedLocations.length;
  }

  class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

    MapView mapView;
    TextView title, subtitle;
    GoogleMap map;
    View layout;

    private ViewHolder(
            View itemView, final RecyclerViewForBoothListAdapter.OnItemClickListener listener) {
      super(itemView);
      layout = itemView;
      mapView = layout.findViewById(R.id.lite_listrow_map);
      title = layout.findViewById(R.id.lite_listrow_text1);
      subtitle = layout.findViewById(R.id.lite_listrow_text2);
      if (mapView != null) {
        // Initialise the MapView
        mapView.onCreate(null);
        // Set the map ready callback to receive the GoogleMap object
        mapView.getMapAsync(this);
        itemView.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    if (listener != null) {
                      int position = getAdapterPosition();
                      if (position != RecyclerView.NO_POSITION) listener.onItemClick(position);
                    }
                  }
                });
      }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
      MapsInitializer.initialize(context);
      map = googleMap;
      setMapLocation();
    }

    private void setMapLocation() {
      if (map == null) return;

      NamedLocation data = (NamedLocation) mapView.getTag();
      if (data == null) return;

      // Add a marker for this item and set the camera
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(data.location, 13f));
      map.addMarker(new MarkerOptions().position(data.location));

      // Set the map type back to normal.
      map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
      map.getUiSettings().setMapToolbarEnabled(false);
      map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
          if (mListener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) mListener.onItemClick(position);
          }
        }
      });
    }

    private void bindView(int pos) {
      NamedLocation item = namedLocations[pos];
      // Store a reference of the ViewHolder object in the layout.
      layout.setTag(this);
      // Store a reference to the item in the mapView's tag. We use it to get the
      // coordinate of a location, when setting the map location.
      mapView.setTag(item);
      setMapLocation();
      title.setText(item.name);
      subtitle.setText(item.landmark);
    }
  }

  public static class NamedLocation {

    public final String name;
    public final String landmark;
    public final LatLng location;

    public NamedLocation(String name, String landmark, LatLng location) {
      this.name = name;
      this.landmark = landmark;
      this.location = location;
    }
  }
}
