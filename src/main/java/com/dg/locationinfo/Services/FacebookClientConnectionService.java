package com.dg.locationinfo.Services;

import com.restfb.DefaultFacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service("facebookService")
public class FacebookClientConnectionService implements ClientConnectionService<DefaultFacebookClient> {
    private final String appID;
    private final String appSecret;
    private final String FB_API_INITIALIZED = "FB API INITIALIZED";
    private final String FB_API_NOT_INITIALIZED = "FB CANNOT INITIALIZE";
    private DefaultFacebookClient client;
    private static final Logger logger = Logger.getLogger(FacebookClientConnectionService.class.getName());

    public FacebookClientConnectionService(@Value("${dg.appId}") String appID,@Value("${dg.appSecret}") String appSecret) {
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
            logger.info(FB_API_INITIALIZED);
        } else {
            logger.severe(FB_API_NOT_INITIALIZED);
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
