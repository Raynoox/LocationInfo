package com.dg.locationinfo;

import com.dg.locationinfo.Services.ClientConnectionService;
import com.restfb.DefaultFacebookClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationInfoApplicationTests {
	@MockBean
	private ClientConnectionService<DefaultFacebookClient> facebookConnectionService;
	@Test
	public void contextLoads() {
		assertTrue(true);
	}

}
