package info.stasha.testosterone.jersey.junit4.integration.test;

import info.stasha.testosterone.annotation.Configuration;

/**
 *
 * @author stasha
 */
// !!! NOTE !!! FOR REAL "EXTERNAL" INTEGRATION TEST, runServer and runDb should be set to false
@Configuration(baseUri = "http://localhost/", httpPort = 9999, runServer = true, runDb = true)
public class TaskEndpointExternalIntegrationTest extends TaskEndpointIntegrationTest {

}
