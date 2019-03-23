package info.stasha.testosterone.servers;

import io.helidon.microprofile.server.Server;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import io.helidon.config.Config;
import io.helidon.microprofile.config.MpConfig;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty server configuration
 *
 * @author stasha
 */
public class HelidonMpServerConfig implements ServerConfig<Application> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelidonMpServerConfig.class);
    private TestConfig testConfig;

    private Server server;
    private ResourceConfig resourceConfig;
    private ServletContainerConfig servletContainerConfig;

    public HelidonMpServerConfig() {
    }

    public HelidonMpServerConfig(TestConfig config) {
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
    public void setConfigurationObject(Application configuration) {
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
        return server != null;
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        if (!isRunning()) {

            server = Server.builder()
                    .addApplication(this.resourceConfig)
                    .cdiContainer(new Weld().initialize())
                    .config(MpConfig.builder().config(Config.create()).build())
                    .host(testConfig.getBaseUri().getHost())
                    .port(testConfig.getHttpPort())
                    .build();

            server.start();
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        if (isRunning() && (testConfig == null || testConfig.isStopServerAfterTestEnds())) {
            server.stop();
        } else if (isRunning() && !testConfig.isStopServerAfterTestEnds()) {
            Thread.currentThread().join();
        }
    }

}
