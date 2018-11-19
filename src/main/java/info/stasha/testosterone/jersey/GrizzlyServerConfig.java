package info.stasha.testosterone.jersey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.Utils;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Grizzly server configuration.
 *
 * @author stasha
 */
public class GrizzlyServerConfig implements ServerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrizzlyServerConfig.class);
    private final TestConfig config;
    private ServletContainerConfig servletContainerConfig;
    private ResourceConfig resourceConfig;
    private final AtomicReference<Client> client = new AtomicReference<>(null);
    private HttpServer server;

    public GrizzlyServerConfig(TestConfig config) {
        this.config = config;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public TestConfig getTestConfig() {
        return this.config;
    }

    /**
     * Returns ResourceConfig.
     *
     * @return
     */
    protected ResourceConfig configure() {
        if (this.resourceConfig == null) {
            this.resourceConfig = new ResourceConfig();
        }

        return this.resourceConfig;
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
     * @return
     */
    @Override
    public Client client() {
        if (client.get() == null) {
            client.getAndSet(getClient());
        }
        return client.get();
    }

    protected Client getClient() {
        ClientConfig clientConfig = new ClientConfig(this.resourceConfig);
        return ClientBuilder.newClient(clientConfig);
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public WebTarget target() {
        try {
            return client().target(getBaseUri());
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Closes client.
     *
     * @param clients
     */
    protected void closeClient(final Client... clients) {
        if (clients == null || clients.length == 0) {
            return;
        }

        for (Client c : clients) {
            if (c == null) {
                continue;
            }
            try {
                c.close();
                client.getAndSet(null);
            } catch (Throwable ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public ResourceConfig getResourceConfig() {
        return configure();
    }

    /**
     * {@inheritDoc }
     *
     * @param resourceConfig
     */
    @Override
    public void setResourceConfig(ResourceConfig resourceConfig) {
        this.resourceConfig = resourceConfig;
    }

    /**
     * Creates server.
     *
     * @throws URISyntaxException
     */
    protected void createServer() throws URISyntaxException {
        server = GrizzlyHttpServerFactory.createHttpServer(getBaseUri(), configure());
    }

    /**
     * Prepares whatever :).
     */
    protected void prepare() {
        Client old = client.getAndSet(getClient());
        closeClient(old);
    }

    /**
     * Cleans up configuration.
     */
    protected void cleanUp() {
        this.resourceConfig = null;

        closeClient(client.get());
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        prepare();

        if (server != null && !server.isStarted()) {
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
        if (server != null && server.isStarted()) {
            server.stop();
        }
        cleanUp();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void init() {
       

        try {
            createServer();

        } catch (Exception ex) {
            LOGGER.error("Failed to create server.", ex);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public URI getBaseUri() {
        return UriBuilder.fromUri(config.getBaseUri()).port(config.getHttpPort()).build();
    }

}
