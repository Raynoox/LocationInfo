package com.dg.locationinfo.Services;

import com.dg.locationinfo.Dao.LocationFacebookDao;
import com.dg.locationinfo.Models.LocationInfo;
import com.dg.locationinfo.Utils.PlaceToLocationInfoTransformer;
import com.restfb.types.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationFacebookProviderService implements LocationProviderService {

    private final LocationFacebookDao dao;

    private final String CITY_ID = "city_id";

    @Autowired
    public LocationFacebookProviderService(LocationFacebookDao dao) {
        this.dao = dao;
    }

    @Override
    public List<LocationInfo> getLocationInformation(String country, String city, String description) {
        List<Place> places = dao.getData(country+','+city+','+description);
        return new PlaceToLocationInfoTransformer().transform(filterPlaces(places, country, city));
    }

    private List<Place> filterPlaces(List<Place> places, String country, String city) {
        return places.stream().filter(place -> place.getLocation() != null
                && place.getLocation().getLongitude() != null
                && place.getLocation().getLatitude() != null
                && country.toUpperCase().equals(place.getLocation().getCountry().toUpperCase())
                && city.toUpperCase().equals(place.getLocation().getCity().toUpperCase())).collect(Collectors.toList());
    }
    //TODO sortByCity, tests

    private Integer extractCityId(String locationAsString) {
        int afterIndex = locationAsString.lastIndexOf(CITY_ID);
        if(afterIndex > -1) {
            int endIndex = locationAsString.indexOf(",",afterIndex);
            return Integer.getInteger(locationAsString.substring(afterIndex+CITY_ID.length(), endIndex));
        }
        return null;
    }

}
