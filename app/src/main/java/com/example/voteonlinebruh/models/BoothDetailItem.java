package com.example.voteonlinebruh.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.StringTokenizer;

public class BoothDetailItem implements Serializable {
  private String area, address, landmark, mapLink;
  private Double lat, lng;

  public BoothDetailItem(
      String area, String address, String landmark, String mapLink, String coordinates) {
    this.area = area;
    this.address = address;
    this.landmark = landmark;
    this.mapLink = mapLink;
    coordinates = coordinates.trim();
    StringTokenizer tokenizer = new StringTokenizer(coordinates, " ,");
    this.lat = Double.parseDouble(tokenizer.nextToken());
    this.lng = Double.parseDouble(tokenizer.nextToken());
  }

  public String getArea() {
    return area;
  }

  public String getAddress() {
    return address;
  }

  public String getLandmark() {
    return landmark;
  }

  public String getMapLink() {
    return mapLink;
  }

  public Double getLat() {
    return lat;
  }

  public Double getLng() {
    return lng;
  }
}
