package info.stasha.testosterone.testng;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;

/**
 *
 * @author stasha
 */
@Configuration(serverStarts = StartServer.PER_TEST, port = 9998)
public class TestPerTest extends SuperTest {
	
}
