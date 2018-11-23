package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.servers.JettyServerConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract test configuration.
 *
 * @author stasha
 * @param <T>
 * @param <C>
 */
public abstract class AbstractTestConfig<T, C> implements TestConfig<T, C> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTestConfig.class);

    private final Set<Throwable> exceptions = new HashSet<>();

    private RestClient client = new RestClient(this);
    private T testosterone;
    private ServerConfig serverConfig;
    private Configuration config;
    private ServletContainerConfig servletContainerConfig;
    private DbConfig dbConfig;
    private Boolean runServer;
    private Boolean runDb;
    private URI baseUri;
    private int httpPort;
    private StartServer startServer;
    private Setup setup;
    private final String mainThreadName;
    private static boolean running;

    public AbstractTestConfig() {
        this(null, null);
    }

    public AbstractTestConfig(T testosterone) {
        this(testosterone, null);
    }

    public AbstractTestConfig(T testosterone, Configuration config) {
        this.testosterone = testosterone;
        this.config = config;
        this.mainThreadName = Thread.currentThread().getName();
    }

    @Override
    public void setTestosterone(T testosterone) {
        this.testosterone = testosterone;
    }

    @Override
    public void setConfig(Configuration config) {
        this.config = config;
    }

    @Override
    public RestClient getClient() {
        return this.client;
    }

    public void setClient(RestClient client) {
        this.client = client;
    }

    @Override
    public T getTest() {
        return testosterone;
    }

    @Override
    public ServerConfig getServerConfig() {
        if (serverConfig == null) {
            this.serverConfig = Utils.loadConfig(
                    DEFAULT_SERVER_CONFIG_PROPERTY,
                    ServerConfig.class,
                    JettyServerConfig.class,
                    (SuperTestosterone) getTest());
            this.serverConfig.setTestConfig(this);
        }

        return this.serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public DbConfig getDbConfig() {
        if (dbConfig == null) {
            this.dbConfig = Utils.loadConfig(
                    DEFAULT_DB_CONFIG_PROPERTY,
                    DbConfig.class,
                    H2Config.class,
                    (SuperTestosterone) getTest());
            this.dbConfig.setTestConfig(this);
        }

        return this.dbConfig;
    }

    public void setDbConfig(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public ServletContainerConfig getServletContainerConfig() {
        if (this.servletContainerConfig == null) {
            this.servletContainerConfig = new ServletContainerConfig(this);
            getServerConfig().setServletContainerConfig(servletContainerConfig);
        }

        return this.servletContainerConfig;
    }

    public void setServletContainerConfig(ServletContainerConfig servletContainerConfig) {
        this.servletContainerConfig = servletContainerConfig;
    }

    public boolean isRunServer() {
        if (this.runServer == null) {
            this.runServer = config != null ? config.runServer() : RUN_SERVER;
        }

        return this.runServer;
    }

    public void setRunServer(Boolean runServer) {
        this.runServer = runServer;
    }

    public boolean isRunDb() {
        if (this.runDb == null) {
            this.runDb = config != null ? config.runDb() : RUN_DB;
        }

        return this.runDb;
    }

    public void setRunDb(Boolean runDb) {
        this.runDb = runDb;
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

    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public int getHttpPort() {
        if (this.httpPort == 0) {
            this.httpPort = config != null ? config.httpPort() : HTTP_PORT;
        }

        return this.httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    @Override
    public StartServer getStartServer() {
        if (this.startServer == null) {
            this.startServer = config != null ? config.startServer() : StartServer.PER_CLASS;
        }

        return this.startServer;
    }

    public void setStartServer(StartServer startServer) {
        this.startServer = startServer;
    }

    @Override
    public Setup getSetup() {
        if (this.setup == null) {
            this.setup = new Setup(this);
        }
        return this.setup;
    }

    public void setSetup(Setup setup) {
        this.setup = setup;
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

    @Override
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Starts server.
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

        if (!isRunning()) {
            running = true;
            getSetup().beforeServerStart((SuperTestosterone) getTest());
            LOGGER.info("Starting server with {} configuration", getStartServer());

            if (isRunDb()) {
                getDbConfig().start();
            } else {
                LOGGER.info("DB is turned off. Use @Configuration(runDb=\"true\") to turn DB on.");
            }
            if (isRunServer()) {
                getServerConfig().start();
            } else {
                LOGGER.info("Server is turned off");
            }

            getClient().start();

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
            if (isRunning()) {

                getClient().stop();
                try {
                    getSetup().beforeServerStop((SuperTestosterone) getTest());
                } catch (Throwable ex) {
                    throw new Error(ex);
                } finally {

                    LOGGER.info("Stopping server configured with: {}", getStartServer());
                    getServerConfig().stop();
                    getDbConfig().stop();
                    try {
                        getSetup().afterServerStop((SuperTestosterone) getTest());
                    } catch (Throwable ex) {
                        throw new Error(ex);
                    } finally {
                        getSetup().clearFlags();

                        running = false;
                        System.out.println("");
                    }
                }
            }
        } finally {
            TestConfigFactory.TEST_CONFIGURATIONS.remove(Utils.getInstrumentedClassName((SuperTestosterone) getTest()));
        }
    }

    @Override
    public String toString() {
        return "DefaultTestConfig{"
                + "startServer=" + this.getStartServer()
                + ", baseUri=" + this.getBaseUri()
                + ", httpPort=" + this.getHttpPort()
                + ", mainThreadName=" + this.getMainThreadName()
                + ", runServer=" + this.isRunServer()
                + ", runDb=" + this.isRunDb()
                + ", testConfig=" + this.getClass().getName()
                + ", testosterone=" + testosterone.getClass().getName()
                + ", serverConfig=" + serverConfig.getClass().getName()
                + ", servletContainerConfig=" + servletContainerConfig.getClass().getName()
                + ", dbConfig=" + dbConfig.getClass().getName()
                + "}";
    }

}
