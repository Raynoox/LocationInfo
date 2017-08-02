package com.dg.locationinfo.Services;

import com.restfb.DefaultFacebookClient;
import com.restfb.exception.FacebookOAuthException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.util.logging.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FacebookClientConnectionServiceTest {

    private FacebookClientConnectionService service;

    private Logger logger = Logger.getLogger(FacebookClientConnectionService.class.getName());
    private Handler handler;
    private ByteArrayOutputStream out;

    private final String FB_API_INITIALIZED = "FB API INITIALIZED";
    private final String FB_API_NOT_INITIALIZED = "FB CANNOT INITIALIZE";

    private final String WRONG_APP_ID = "ID123456789";
    private final String WRONG_APP_SECRET = "SECRET123456789";
    @MockBean
    private ClientConnectionService<DefaultFacebookClient> facebookConnectionService;

    @Value("${dg.appID}")
    private String CORRECT_APP_ID;
    @Value("${dg.appSecret}")
    private String CORRECT_APP_SECRET;

    @Before
    public void setUp() throws Exception {
        Formatter formatter = new SimpleFormatter();
        out = new ByteArrayOutputStream();
        handler = new StreamHandler(out, formatter);
        logger.addHandler(handler);
    }

    @After
    public void tearDown() throws Exception {
        logger.removeHandler(handler);
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

    @Test
    public void testNotInitializedStatus() {
        service = new FacebookClientConnectionService("","");
        service.logClientStatus();
        handler.flush();
        String loggerMessage = out.toString();
        assertNotNull(loggerMessage);
        assertTrue(loggerMessage.toUpperCase().contains(FB_API_NOT_INITIALIZED));
    }

}