package org.unlucky.gpsmover.db;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class FavoriteLocation {
    private int id;
    private String title;
    private double latitude;
    private double longitude;
    private float zoomLevel;

    public FavoriteLocation(String title, double latitude, double longitude) {
        this(0, title, latitude, longitude, 15);
    }

    public FavoriteLocation(String title, double latitude, double longitude, float zoomLevel) {
        this(0, title, latitude, longitude, zoomLevel);
    }

    public FavoriteLocation(int id, String title, double latitude, double longitude, float zoomLevel) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoomLevel = zoomLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public void setLocation(LatLng latlng) {
        this.latitude = latlng.latitude;
        this.longitude = latlng.longitude;
    }
}
