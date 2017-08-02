package com.dg.locationinfo.Models;

import com.restfb.types.Place;

public class LocationInfo {
    private String name;
    private Double latitude;
    private Double longitude;

    public LocationInfo(Place place) {
        this.name = place.getName();
        if(place.getLocation() != null) {
            this.latitude = place.getLocation().getLatitude();
            this.longitude = place.getLocation().getLongitude();
        }
    }

    public LocationInfo(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
