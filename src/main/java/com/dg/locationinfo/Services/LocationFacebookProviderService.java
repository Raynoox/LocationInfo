package com.dg.locationinfo.Services;

import com.dg.locationinfo.Dao.LocationFacebookDao;
import com.dg.locationinfo.Models.LocationInfo;
import com.dg.locationinfo.Utils.PlaceToLocationInfoTransformer;
import com.restfb.types.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationFacebookProviderService implements LocationProviderService {

    private final LocationFacebookDao dao;

    private final String CITY_ID = "\"city_id\":";

    @Autowired
    public LocationFacebookProviderService(LocationFacebookDao dao) {
        this.dao = dao;
    }

    @Override
    public List<List<LocationInfo>> getLocationInformation(String country, String city, String description) {
        List<Place> places = dao.getData(country+','+city+','+description);
        places = filterPlaces(places, country, city);
        PlaceToLocationInfoTransformer transformer = new PlaceToLocationInfoTransformer();
        return processLocations(places)
                .entrySet()
                .stream()
                .map(entry -> transformer.transform(entry.getValue()))
                .collect(Collectors.toList());
    }

    private HashMap<Integer, List<Place>> processLocations(List<Place> places) {
        HashMap<Integer, List<Place>> map = new HashMap<>();
        places.forEach(place -> {
            Integer cityId = extractCityId(place.getLocationAsString());
            if (map.containsKey(cityId)) {
                map.get(cityId).add(place);
            } else {
                List<Place> singlePlace = new ArrayList<>();
                singlePlace.add(place);
                map.put(cityId, singlePlace);
            }
        });
        return map;
    }
    private List<Place> filterPlaces(List<Place> places, String country, String city) {
        return places.stream().filter(place -> place.getLocation() != null
                && place.getLocation().getLongitude() != null
                && place.getLocation().getLatitude() != null
                && place.getLocation().getCountry() != null
                && place.getLocation().getCity() != null
                && country.toUpperCase().equals(place.getLocation().getCountry().toUpperCase())
                && city.toUpperCase().equals(place.getLocation().getCity().toUpperCase()))
                .collect(Collectors.toList());
    }

    private Integer extractCityId(String locationAsString) {

        int afterIndex = locationAsString.lastIndexOf(CITY_ID);
        if(afterIndex > -1) {
            int endIndex = locationAsString.indexOf(",",afterIndex);
            return Integer.parseInt(locationAsString.substring(afterIndex+CITY_ID.length(), endIndex));
        }
        return null;
    }

}
