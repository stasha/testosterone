package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.servers.JettyServerConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default test configuration.
 *
 * @author stasha
 * @param <T>
 * @param <C>
 */
public abstract class DefaultTestConfig<T, C> implements TestConfig<T, C> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTestConfig.class);

    private final RestClient client = new RestClient(this);
    private final Set<Throwable> exceptions = new HashSet<>();

    protected final T testosterone;
    protected ServerConfig serverConfig;
    private final Configuration config;
    private ServletContainerConfig servletContainerConfig;
    private DbConfig dbConfig;
    private URI baseUri;
    private int httpPort;
    private StartServer startServer;
    private Setup setup;
    private final String mainThreadName;

    public DefaultTestConfig(T testosterone) {
        this(testosterone, null);
    }

    public DefaultTestConfig(T testosterone, Configuration config) {
        this.testosterone = testosterone;
        this.config = config;
        this.mainThreadName = Thread.currentThread().getName();
    }

    @Override
    public T getTest() {
        return testosterone;
    }

    @Override
    public ServerConfig getServerConfig() {
        if (serverConfig == null) {
            this.serverConfig = new JettyServerConfig(this);
        }

        return this.serverConfig;
    }

    @Override
    public RestClient getClient() {
        return this.client;
    }

    @Override
    public ServletContainerConfig getServletContainerConfig() {
        if (this.servletContainerConfig == null) {
            this.servletContainerConfig = new ServletContainerConfig(this);
            this.serverConfig.setServletContainerConfig(servletContainerConfig);
        }

        return this.servletContainerConfig;
    }

    @Override
    public DbConfig getDbConfig() {
        if (dbConfig == null) {
            this.dbConfig = new H2Config(this);
        }

        return this.dbConfig;
    }

    @Override
    public TestExecutor getTestExecutor(Method method, SuperTestosterone test) {
        return new TestExecutorImpl(method, test);
    }

    @Override
    public URI getBaseUri() {
        if (this.baseUri == null) {
            String uri = config != null ? config.baseUri() : BASE_URI;
            this.baseUri = UriBuilder.fromUri(uri).port(getHttpPort()).build();
        }

        return this.baseUri;
    }

    @Override
    public int getHttpPort() {
        if (this.httpPort == 0) {
            this.httpPort = config != null ? config.httpPort() : HTTP_PORT;
        }

        return this.httpPort;
    }

    @Override
    public StartServer getStartServer() {
        if (this.startServer == null) {
            this.startServer = config != null ? config.startServer() : StartServer.PER_CLASS;
        }

        return this.startServer;
    }

    @Override
    public Setup getSetup() {
        if (this.setup == null) {
            this.setup = new Setup(this);
        }
        return this.setup;
    }

    @Override
    public String getMainThreadName() {
        return this.mainThreadName;
    }

    @Override
    public Set<Throwable> getExceptions() {
        return exceptions;
    }

    @Override
    public void throwExceptions() throws Throwable {
        for (Throwable ex : exceptions) {
            // this will come only to first exception,
            // bot for now it is good enough
            throw ex;
        }
    }

    /**
     * Starts server.
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

        if (!getServerConfig().isRunning()) {
            getSetup().beforeServerStart((SuperTestosterone) getTest());
            LOGGER.info("Starting server with {} configuration", getStartServer());

            getDbConfig().start();
            getClient().start();
            getServerConfig().start();

            LOGGER.info(this.toString());
            // Invoke afterServerStart only if resource is singleton.
            // If there is no Singleton annotation, afterServerStart is 
            // invoked by @PostConstruct interceptor
            if (Utils.isAnnotationPresent(getTest(), Singleton.class)) {
                getSetup().afterServerStart((SuperTestosterone) getTest());
            }
        }
    }

    /**
     * Stops server.
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        try {
            if (getServerConfig().isRunning()) {
                getClient().stop();
                getSetup().beforeServerStop((SuperTestosterone) getTest());

                LOGGER.info("Stopping server configured with: {}", getStartServer());
                getServerConfig().stop();
                getDbConfig().stop();
                getSetup().afterServerStop((SuperTestosterone) getTest());
                getSetup().clearFlags();

                System.out.println("");
            }
        } finally {
            TestConfigFactory.TEST_CONFIGURATIONS.remove(Utils.getInstrumentedClassName((SuperTestosterone) getTest()));
        }
    }

    @Override
    public boolean isRunning() {
        return getServerConfig().isRunning();
    }

    @Override
    public String toString() {
        return "DefaultTestConfig{"
                + "startServer=" + startServer
                + ", baseUri=" + baseUri
                + ", httpPort=" + httpPort
                + ", mainThreadName=" + mainThreadName
                + ", testConfig=" + this.getClass().getName()
                + ", testosterone=" + testosterone.getClass().getName()
                + ", serverConfig=" + serverConfig.getClass().getName()
                + ", servletContainerConfig=" + servletContainerConfig.getClass().getName()
                + ", dbConfig=" + dbConfig.getClass().getName()
                + "}";
    }

}
