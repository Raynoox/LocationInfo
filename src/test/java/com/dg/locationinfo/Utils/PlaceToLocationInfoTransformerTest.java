package com.dg.locationinfo.Utils;

import com.dg.locationinfo.Models.LocationInfo;
import com.dg.locationinfo.TestDataBuilders.PlaceBuilder;
import com.restfb.types.Place;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
public class PlaceToLocationInfoTransformerTest {
    private final PlaceToLocationInfoTransformer transformer = new PlaceToLocationInfoTransformer();

    private final String FIRST_NAME = "firstname";
    private final Double FIRST_LONGITUDE = 123.345;
    private final Double FIRST_LATITUDE = 234.567;

    private final String SECOND_NAME = "secondname";
    private final Double SECOND_LONGITUDE = 456.789;
    private final Double SECOND_LATITUDE = 987.654;
    @Test
    public void testTransform() throws Exception {

        List<Place> places = Arrays.asList(getPlace(FIRST_NAME,FIRST_LONGITUDE,FIRST_LATITUDE), getPlace(SECOND_NAME,SECOND_LONGITUDE,SECOND_LATITUDE));
        List<LocationInfo> result = transformer.transform(places);
        assertEquals(places.size(), result.size());

        assertEquals(FIRST_NAME, result.get(0).getName());
        assertEquals(FIRST_LONGITUDE, result.get(0).getLongitude());
        assertEquals(FIRST_LATITUDE, result.get(0).getLatitude());

        assertEquals(SECOND_NAME, result.get(1).getName());
        assertEquals(SECOND_LONGITUDE, result.get(1).getLongitude());
        assertEquals(SECOND_LATITUDE, result.get(1).getLatitude());
    }
    private Place getPlace(String name, Double longitude, Double latitude) {
        return new PlaceBuilder().withName(name).withLatitude(latitude).withLongitude(longitude).build();
    }
}