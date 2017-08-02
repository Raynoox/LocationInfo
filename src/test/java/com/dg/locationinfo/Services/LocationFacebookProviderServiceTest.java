package com.dg.locationinfo.Services;

import com.dg.locationinfo.Dao.LocationFacebookDao;
import com.dg.locationinfo.Models.LocationInfo;
import com.dg.locationinfo.TestDataBuilders.PlaceBuilder;
import com.restfb.types.Place;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LocationFacebookProviderService.class)
public class LocationFacebookProviderServiceTest {

    @MockBean
    LocationFacebookDao dao;
    @Autowired LocationFacebookProviderService service;

    private final String FIRST_NAME = "firstname";
    private final Double FIRST_LONGITUDE = 123.345;
    private final Double FIRST_LATITUDE = 234.567;

    private final String SECOND_NAME = "secondname";
    private final Double SECOND_LONGITUDE = 456.789;
    private final Double SECOND_LATITUDE = 987.654;

    private final String WRONG_COUNTRY = "wrongCountry";
    private final String WRONG_CITY = "wrongCity";

    private final String COUNTRY = "country";
    private final String CITY = "city";
    private final Integer CITY_ID = CITY.hashCode();
    private final Integer ANOTHER_CITY_ID = CITY_ID.hashCode()+123456;
    private final String DESCRIPTION = "description";
    @Test
    public void returnSingleResult() throws Exception {
        String query = COUNTRY+","+CITY+","+DESCRIPTION;
        List<Place> places = Collections.singletonList(getPlace(FIRST_NAME, FIRST_LONGITUDE, FIRST_LATITUDE, COUNTRY, CITY, CITY_ID));
        when(dao.getData(query)).thenReturn(places);
        List<List<LocationInfo>> result = service.getLocationInformation(COUNTRY,CITY,DESCRIPTION);
        assertEquals(1,result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(FIRST_NAME, result.get(0).get(0).getName());
        assertEquals(FIRST_LONGITUDE, result.get(0).get(0).getLongitude());
        assertEquals(FIRST_LATITUDE, result.get(0).get(0).getLatitude());

    }

    @Test
    public void returnMultipleResults() throws Exception {
        String query = COUNTRY+","+CITY+","+DESCRIPTION;
        List<Place> places = Arrays.asList(getPlace(FIRST_NAME,FIRST_LONGITUDE,FIRST_LATITUDE,COUNTRY,CITY, CITY_ID), getPlace(SECOND_NAME,SECOND_LONGITUDE,SECOND_LATITUDE,COUNTRY,CITY, CITY_ID));
        when(dao.getData(query)).thenReturn(places);
        List<List<LocationInfo>> result = service.getLocationInformation(COUNTRY,CITY,DESCRIPTION);
        assertEquals(1, result.size());
        assertEquals(2,result.get(0).size());
        assertEquals(FIRST_NAME, result.get(0).get(0).getName());
        assertEquals(FIRST_LONGITUDE, result.get(0).get(0).getLongitude());
        assertEquals(FIRST_LATITUDE, result.get(0).get(0).getLatitude());
        assertEquals(SECOND_NAME, result.get(0).get(1).getName());
        assertEquals(SECOND_LONGITUDE, result.get(0).get(1).getLongitude());
        assertEquals(SECOND_LATITUDE, result.get(0).get(1).getLatitude());
    }

    @Test
    public void returnOneObjectWhenCountryWrong() throws Exception {
        String query = COUNTRY+","+CITY+","+DESCRIPTION;
        List<Place> places = Arrays.asList(getPlace(FIRST_NAME,FIRST_LONGITUDE,FIRST_LATITUDE,WRONG_COUNTRY,WRONG_CITY,CITY_ID), getPlace(SECOND_NAME,SECOND_LONGITUDE,SECOND_LATITUDE,COUNTRY,CITY,CITY_ID));
        when(dao.getData(query)).thenReturn(places);
        List<List<LocationInfo>> result = service.getLocationInformation(COUNTRY,CITY,DESCRIPTION);
        assertEquals(1,result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(SECOND_NAME, result.get(0).get(0).getName());
        assertEquals(SECOND_LONGITUDE, result.get(0).get(0).getLongitude());
        assertEquals(SECOND_LATITUDE, result.get(0).get(0).getLatitude());
    }
    @Test
    public void returnEmptyArrayIfCountryWrong() throws Exception {
        String query = COUNTRY+","+CITY+","+DESCRIPTION;
        List<Place> places = Arrays.asList(getPlace(FIRST_NAME,FIRST_LONGITUDE,FIRST_LATITUDE,WRONG_COUNTRY,WRONG_CITY,CITY_ID), getPlace(SECOND_NAME,SECOND_LONGITUDE,SECOND_LATITUDE,WRONG_COUNTRY,WRONG_CITY,CITY_ID));
        when(dao.getData(query)).thenReturn(places);
        List<List<LocationInfo>> result = service.getLocationInformation(COUNTRY,CITY,DESCRIPTION);
        assertEquals(0,result.size());
    }
    @Test
    public void separateTwoDifferentCitiesWithSameName() throws Exception {
        String query = COUNTRY+","+CITY+","+DESCRIPTION;
        List<Place> places = Arrays.asList(getPlace(FIRST_NAME,FIRST_LONGITUDE,FIRST_LATITUDE,COUNTRY,CITY, CITY_ID), getPlace(SECOND_NAME,SECOND_LONGITUDE,SECOND_LATITUDE,COUNTRY,CITY, ANOTHER_CITY_ID));
        when(dao.getData(query)).thenReturn(places);
        List<List<LocationInfo>> result = service.getLocationInformation(COUNTRY,CITY,DESCRIPTION);
        assertEquals(2, result.size());
        assertEquals(1,result.get(0).size());
        assertEquals(1,result.get(1).size());

        assertEquals(FIRST_NAME, result.get(0).get(0).getName());
        assertEquals(FIRST_LONGITUDE, result.get(0).get(0).getLongitude());
        assertEquals(FIRST_LATITUDE, result.get(0).get(0).getLatitude());
        assertEquals(SECOND_NAME, result.get(1).get(0).getName());
        assertEquals(SECOND_LONGITUDE, result.get(1).get(0).getLongitude());
        assertEquals(SECOND_LATITUDE, result.get(1).get(0).getLatitude());
    }
    private Place getPlace(String name, Double longitude, Double latitude, String country, String city, Integer city_id) {
        return new PlaceBuilder().withName(name).withLatitude(latitude).withLongitude(longitude).withCountry(country).withCity(city).withCityId(city_id).build();
    }

}