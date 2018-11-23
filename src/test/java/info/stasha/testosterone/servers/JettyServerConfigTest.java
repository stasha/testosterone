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
public class JettyServerConfigTest {

    TestConfig tc = new JerseyTestConfig();
    ServletContainerConfig sc = new ServletContainerConfig(tc);

    @Test
    public void jettyTest() {
        JettyServerConfig config = new JettyServerConfig();
        config.setTestConfig(tc);
        config.setServletContainerConfig(sc);

        assertEquals("TestConfig should equal", tc, config.getTestConfig());
        assertEquals("ServletContainerConfig should equal", sc, config.getServletContainerConfig());
    }

    @Test
    public void jettyTest2() {
        JettyServerConfig config = new JettyServerConfig(tc);
        assertEquals("TestConfig should equal", tc, config.getTestConfig());
    }

}
