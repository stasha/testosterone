package info.stasha.testosterone.servers;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Grizzly server configuration.
 *
 * @author stasha
 */
public class GrizzlyServerConfig implements ServerConfig<ResourceConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrizzlyServerConfig.class);
    private TestConfig testConfig;

    private HttpServer server;
    private ResourceConfig resourceConfig;
    private ServletContainerConfig servletContainerConfig;

    public GrizzlyServerConfig() {
    }

    public GrizzlyServerConfig(TestConfig config) {
        this.testConfig = config;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public TestConfig getTestConfig() {
        return this.testConfig;
    }

    /**
     * {@inheritDoc }
     *
     * @param testConfig
     */
    @Override
    public void setTestConfig(TestConfig testConfig) {
        this.testConfig = testConfig;
    }

    /**
     * {@inheritDoc }
     *
     * @param configuration
     */
    @Override
    public void setConfigurationObject(ResourceConfig configuration) {
        this.resourceConfig = (ResourceConfig) configuration;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public ServletContainerConfig getServletContainerConfig() {
        return this.servletContainerConfig;
    }

    /**
     * {@inheritDoc }
     *
     * @param config
     */
    @Override
    public void setServletContainerConfig(ServletContainerConfig config) {
        this.servletContainerConfig = config;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return server != null && server.isStarted();
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

        if (!isRunning()) {
            try {
                server = GrizzlyHttpServerFactory.createHttpServer(this.testConfig.getBaseUri(), this.resourceConfig);
            } catch (Exception ex) {
                LOGGER.error("Failed to create server.", ex);
            }
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        if (isRunning()) {
            server.stop();
        }
    }

}
