package info.stasha.testosterone.jersey.junit4.integration.test;

import info.stasha.testosterone.annotation.Configuration;

/**
 *
 * @author stasha
 */
@Configuration(baseUri = "http://localhost/", httpPort = 9999, runServer = true, runDb = true)
public class UserEndpointExternalIntegrationTest extends UserEndpointIntegrationTest {

}
