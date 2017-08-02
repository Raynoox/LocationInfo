package com.dg.locationinfo.Dao;

import com.dg.locationinfo.Services.ClientConnectionService;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LocationFacebookDao {

    private final String AFTER_STRING = "&after=";
    private final String SEARCH_TYPE = "place";
    private final String SEARCH_FIELDS = "name,location{country,city,city_id,longitude,latitude}";

    private final ClientConnectionService<DefaultFacebookClient> service;
    @Autowired
    public LocationFacebookDao(ClientConnectionService<DefaultFacebookClient> service) {
        this.service = service;
    }
    public List<Place> getData(String query) {
        List<Place> result = new ArrayList<>();
        String next = "";
        do {
            Connection<Place> p = service.getClient().fetchConnection("search", Place.class,
                    Parameter.with("q", query),
                    Parameter.with("type",SEARCH_TYPE),
                    Parameter.with("fields",SEARCH_FIELDS),
                    Parameter.with("after", extractAfter(next)));
            result.addAll(p.getData());
            next = p.getNextPageUrl();
        } while(next != null && next.length() > 0);
        return result;
    }

    private String extractAfter(String nextUrl) {
        int afterIndex = nextUrl.lastIndexOf(AFTER_STRING);
        return afterIndex > -1 ? nextUrl.substring(afterIndex+AFTER_STRING.length(), nextUrl.length()) : "";
    }
}
