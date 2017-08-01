package com.dg.locationinfo.TestDataBuilders;

import com.restfb.types.Location;
import com.restfb.types.Place;

public class PlaceBuilder {
    private String name;
    private Double latitude;
    private Double longitude;
    private String country;
    private String city;

    public PlaceBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public PlaceBuilder withLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }
    public PlaceBuilder withLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }
    public PlaceBuilder withCity(String city) {
        this.city = city;
        return this;
    }
    public PlaceBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public Place build() {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setCountry(country);
        location.setCity(city);
        Place place = new Place();
        place.setLocation(location);
        place.setName(name);
        return place;
    }
}
