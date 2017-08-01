package com.dg.locationinfo.TestDataBuilders;

import com.dg.locationinfo.Models.LocationInfo;

public class LocationInfoBuilder {
    private String name;
    private Double latitude;
    private Double longitude;

    public LocationInfoBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public LocationInfoBuilder withLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }
    public LocationInfoBuilder withLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }
    public LocationInfo build() {
        return new LocationInfo(name, latitude, longitude);
    }
}
