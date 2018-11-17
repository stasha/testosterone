package info.stasha.testosterone.testng;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;
import org.testng.annotations.Test;

/**
 *
 * @author stasha
 */
@Configuration(startServer = StartServer.PER_TEST_METHOD, httpPort = 9998)
public class TestPerTest extends SuperTest {

    @Test
    public void test() {

    }

}
