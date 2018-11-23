package info.stasha.testosterone.servers;

import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class GrizzlyServerConfigTest {

    @Test
    public void jettyTest() {
        TestConfig tc = new JerseyTestConfig();
        ServletContainerConfig sc = new ServletContainerConfig(tc);
        GrizzlyServerConfig config = new GrizzlyServerConfig();
        config.setTestConfig(tc);
        config.setServletContainerConfig(sc);

        assertEquals("TestConfig should equal", tc, config.getTestConfig());
        assertEquals("ServletContainerConfig should equal", sc, config.getServletContainerConfig());
    }

}
