package info.stasha.testosterone.testng;

import info.stasha.testosterone.annotation.Configuration;

/**
 *
 * @author stasha
 */
@Configuration(serverStarts = Configuration.ServerStarts.PER_TEST, port = 9998)
public class TestPerTest extends SuperTest {
	
}
