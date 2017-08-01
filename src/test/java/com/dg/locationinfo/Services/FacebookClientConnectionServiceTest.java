package com.dg.locationinfo.Services;

import com.dg.locationinfo.LocationinfoApplication;
import com.restfb.exception.FacebookOAuthException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LocationinfoApplication.class)
public class FacebookClientConnectionServiceTest {

    private FacebookClientConnectionService service;

    private final String WRONG_APP_ID = "ID123456789";
    private final String WRONG_APP_SECRET = "SECRET123456789";
    @Value("${AppID}")
    private String CORRECT_APP_ID;
    @Value("${AppSecret}")
    private String CORRECT_APP_SECRET;

    @Test
    public void testFBInitializationSuccess() {
        service = new FacebookClientConnectionService(CORRECT_APP_ID, CORRECT_APP_SECRET);
        service.connect();
        assertNotNull(service.getClient());
    }

    @Test(expected = FacebookOAuthException.class)
    public void testWrongAppSecret() {
        service = new FacebookClientConnectionService(CORRECT_APP_ID, WRONG_APP_SECRET);
        service.connect();
        assertNull(service.getClient());
    }

    @Test(expected = FacebookOAuthException.class)
    public void testWrongAppId() {
        service = new FacebookClientConnectionService(WRONG_APP_ID, CORRECT_APP_SECRET);
        service.connect();
        assertNull(service.getClient());
    }
}