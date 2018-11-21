package info.stasha.testosterone.jersey.junit4.servers;

import info.stasha.testosterone.jersey.junit4.*;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servers.GrizzlyServerConfig;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(serverConfig = GrizzlyServerConfig.class)
public class GrizzlyTest implements Testosterone {

    @Test
    @Request(url = "fdsafds", expectedStatus = 404)
    public void test(Response resp) {
    }
    
    @Test
    public void assertGrizzly() {
        ServerConfig config = this.getTestConfig().getServerConfig();
        
        assertNotNull("Test config should not be null", config.getTestConfig());
        config.setTestConfig(null);
        assertNull("Test config should be null", config.getTestConfig());
        assertNotNull("ServletContainer config should not be null", config.getServletContainerConfig());
    }
}
