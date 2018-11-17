package info.stasha.testosterone;

import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Testosterone configuration interface.
 *
 * @author stasha
 */
public interface ServerConfig {

    /**
     * Returns test configuration.
     *
     * @return
     */
    TestConfig getTestConfig();

    /**
     * Returns ResourceConfig instance.
     *
     * @return
     */
    ResourceConfig getResourceConfig();

    /**
     * Sets ResourceConfig.
     *
     * @param resourceConfig
     */
    void setResourceConfig(ResourceConfig resourceConfig);

    /**
     * Returns Jersey Client instance.
     *
     * @return
     */
    Client client();

    /**
     * Returns WebTarget instance for invoking http requests.
     *
     * @return
     */
    WebTarget target();

    /**
     * Returns base uri.
     *
     * @return
     */
    URI getBaseUri();

    /**
     * Returns servlet configuration object where context params, listeners,
     * filters and servlets can be registered.
     *
     * @return
     */
    ServletContainerConfig getServletContainerConfig();

    /**
     * Sets servlet container config.
     *
     * @param config
     */
    void setServletContainerConfig(ServletContainerConfig config);

    /**
     * Initialize configuration.
     *
     * @throws Exception
     */
    void init();

    /**
     * Returns true false if server is running.
     *
     * @return
     */
    boolean isRunning();

    /**
     * Starts server.
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * Stops server.
     *
     * @throws Exception
     */
    void stop() throws Exception;

}
