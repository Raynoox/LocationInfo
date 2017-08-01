package com.dg.locationinfo.Services;

import com.restfb.DefaultFacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service
public class FacebookClientConnectionService implements ClientConnectionService<DefaultFacebookClient> {
    private final String appID;
    private final String appSecret;

    private DefaultFacebookClient client;
    private static final Logger logger = Logger.getLogger(String.valueOf(FacebookClientConnectionService.class));

    public FacebookClientConnectionService(@Value("${AppID}") String appID,@Value("${AppSecret}") String appSecret) {
        this.appID = appID;
        this.appSecret = appSecret;
    }

    @PostConstruct
    public void connect() throws FacebookOAuthException {
            this.setClient(new DefaultFacebookClient(obtainAccessToken(), Version.VERSION_2_9));
            logClientStatus();
    }

    public void logClientStatus() {
        if(this.getClient() != null) {
            logger.info("FB API INITIALIZED");
        } else {
            logger.severe("FB CANNOT INITIALIZE");
        }
    }

        public DefaultFacebookClient getClient() {
        return client;
    }

    private void setClient(DefaultFacebookClient client) {
        this.client = client;
    }
    private String obtainAccessToken() {

        return new DefaultFacebookClient(Version.VERSION_2_9).obtainAppAccessToken(appID, appSecret).getAccessToken();
    }
}
