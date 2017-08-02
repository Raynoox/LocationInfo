package com.dg.locationinfo.Dao;

import com.dg.locationinfo.Services.ClientConnectionService;
import com.dg.locationinfo.TestDataBuilders.PlaceBuilder;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Place;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LocationFacebookDao.class)
public class LocationFacebookDaoTest {
    @MockBean
    ClientConnectionService<DefaultFacebookClient> facebookService;
    @Mock
    private DefaultFacebookClient client;
    @Autowired
    LocationFacebookDao dao;
    @Mock
    private Connection<Place> placeMock;
    @Mock
    private
    Connection<Place> placeMockNext;
    private final String AFTER_STRING = "&after=";
    private final String SEARCH_TYPE = "place";
    private final String SEARCH_FIELDS = "name,location{country,city,city_id,longitude,latitude}";
    private final String QUERY = "COUNTRY,CITY,DESCRIPTION";

    private final String NEXT_PAGE = "ABCDEFG";

    private final String FIRST_NAME = "firstname";
    private final Double FIRST_LONGITUDE = 123.345;
    private final Double FIRST_LATITUDE = 234.567;

    private final String SECOND_NAME = "secondname";
    private final Double SECOND_LONGITUDE = 456.789;
    private final Double SECOND_LATITUDE = 987.654;

    @Before
    public void setUp() throws Exception {
        when(facebookService.getClient()).thenReturn(client);
    }
    @Test
    public void returnsAllWhenNextProvided() throws Exception {
        List<Place> places = Collections.singletonList(getPlace(FIRST_NAME,FIRST_LATITUDE,FIRST_LONGITUDE));
        List<Place> placesNext = Collections.singletonList(getPlace(SECOND_NAME,SECOND_LATITUDE,SECOND_LONGITUDE));

        when(placeMock.getData()).thenReturn(places);
        when(placeMock.getNextPageUrl()).thenReturn(AFTER_STRING+NEXT_PAGE);
        when(placeMockNext.getData()).thenReturn(placesNext);

        when(client.fetchConnection("search",Place.class,
                Parameter.with("q", QUERY),
                Parameter.with("type",SEARCH_TYPE),
                Parameter.with("fields",SEARCH_FIELDS),
                Parameter.with("after", ""))).thenReturn(placeMock);
        when(client.fetchConnection("search",Place.class,
                Parameter.with("q", QUERY),
                Parameter.with("type",SEARCH_TYPE),
                Parameter.with("fields",SEARCH_FIELDS),
                Parameter.with("after", NEXT_PAGE))).thenReturn(placeMockNext);
        List<Place> data = dao.getData(QUERY);
        assertEquals(2, data.size());
        verify(facebookService,times(2)).getClient();
        verify(client,times(1)).fetchConnection("search",Place.class,
                Parameter.with("q", QUERY),
                Parameter.with("type",SEARCH_TYPE),
                Parameter.with("fields",SEARCH_FIELDS),
                Parameter.with("after", ""));
        verify(client,times(1)).fetchConnection("search",Place.class,
                Parameter.with("q", QUERY),
                Parameter.with("type",SEARCH_TYPE),
                Parameter.with("fields",SEARCH_FIELDS),
                Parameter.with("after", NEXT_PAGE));
    }
    @Test
    public void returnsEmptyIfNoResults() throws Exception {
        List<Place> places = Collections.emptyList();
        when(placeMock.getData()).thenReturn(places);
        when(client.fetchConnection("search",Place.class,
                Parameter.with("q", QUERY),
                Parameter.with("type",SEARCH_TYPE),
                Parameter.with("fields",SEARCH_FIELDS),
                Parameter.with("after", ""))).thenReturn(placeMock);
        List<Place> data = dao.getData(QUERY);
        assertEquals(0, data.size());
        verify(facebookService,times(1)).getClient();
        verify(client,times(1)).fetchConnection("search",Place.class,
                Parameter.with("q", QUERY),
                Parameter.with("type",SEARCH_TYPE),
                Parameter.with("fields",SEARCH_FIELDS),
                Parameter.with("after", ""));
    }
    private Place getPlace(String name, Double longitude, Double latitude) {
        return new PlaceBuilder().withName(name).withLatitude(latitude).withLongitude(longitude).build();
    }
}