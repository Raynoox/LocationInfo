package com.dg.locationinfo.Controllers;

import com.dg.locationinfo.LocationinfoApplication;
import com.dg.locationinfo.Models.LocationInfo;
import com.dg.locationinfo.Services.LocationProviderService;
import com.dg.locationinfo.TestDataBuilders.LocationInfoBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = LocationinfoApplication.class)
public class LocationInfoControllerTest {

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private final String COUNTRY = "Poland";
    private final String CITY = "Poznan";
    private final String DESCRIPTION = "PP";

    private final String NAME = "PUT poznan";
    private final Double LATITUDE = 52.491234567;
    private final Double LONGITUDE = 16.991234567;
    @MockBean
    private LocationProviderService locationServiceMock;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testEgnyteLocationInformation() throws Exception {
        when(locationServiceMock.getLocationInformation(COUNTRY, CITY, DESCRIPTION)).thenReturn(Collections.singletonList(getLocationInfo()));
        mockMvc.perform((get("/"+ COUNTRY +"/"+ CITY +"/"+ DESCRIPTION)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].latitude").value(LATITUDE))
                .andExpect(jsonPath("$[0].longitude").value(LONGITUDE));
        verify(locationServiceMock, times(1)).getLocationInformation(COUNTRY, CITY, DESCRIPTION);
        verifyNoMoreInteractions(locationServiceMock);
    }

    @Test
    public void testNoneFoundReturnsEmpty() throws Exception {
        when(locationServiceMock.getLocationInformation(CITY, COUNTRY, DESCRIPTION)).thenReturn(Collections.emptyList());
        mockMvc.perform((get("/"+ CITY +"/"+ COUNTRY +"/"+ DESCRIPTION)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$").value(hasSize(0)));
        verify(locationServiceMock, times(1)).getLocationInformation(CITY, COUNTRY, DESCRIPTION);

        verifyNoMoreInteractions(locationServiceMock);
    }

    private LocationInfo getLocationInfo() {
        return new LocationInfoBuilder().withName(NAME).withLatitude(LATITUDE).withLongitude(LONGITUDE).build();
    }

}