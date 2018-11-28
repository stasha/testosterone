package info.stasha.testosterone.jersey.junit4.integration.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * 
 * @author stasha
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    UserEndpointExternalIntegrationTest.class,
    TaskEndpointExternalIntegrationTest.class
})
public class IntegrationTestSuiteTest {
    
}
