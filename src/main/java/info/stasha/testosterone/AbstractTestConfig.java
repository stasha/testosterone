package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.DontIntercept;
import info.stasha.testosterone.cdi.CdiConfig;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.servers.JettyServerConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
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

    public static int totalTestsExecuted;

    private final Set<Throwable> exceptions = new HashSet<>();

    private RestClient client;
    private T testosterone;
    private ServerConfig serverConfig;
    private Configuration config;
    private ServletContainerConfig servletContainerConfig;
    private DbConfig dbConfig;
    private CdiConfig cdiConfig;
    private Boolean runServer;
    private Boolean runDb;
    private URI baseUri;
    private int httpPort;
    private StartServer startServer;
    private Boolean stopServerAfterTestEnds;
    private Setup setup;
    private final String mainThreadName;
    private static boolean running;
    private Set<Method> testMethods = new LinkedHashSet<>();
    private final Set<Method> executedTests = new LinkedHashSet<>();

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

    /**
     * {@inheritDoc }
     *
     * @param testosterone
     */
    @Override
    public void setTestosterone(T testosterone) {
        this.testosterone = testosterone;
    }

    /**
     * {@inheritDoc }
     *
     * @param config
     */
    @Override
    public void setConfig(Configuration config) {
        this.config = config;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public RestClient getClient() {
        if (this.client == null) {
            this.setClient(new RestClient(this));
        }
        return this.client;
    }

    /**
     * Sets the rest client that will be used for running tests.
     *
     * @param client
     */
    public void setClient(RestClient client) {
        this.client = client;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public T getTest() {
        return testosterone;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public ServerConfig getServerConfig() {
        if (serverConfig == null) {
            this.setServerConfig(Utils.loadConfig(
                    DEFAULT_SERVER_CONFIG_PROPERTY,
                    ServerConfig.class,
                    JettyServerConfig.class,
                    (SuperTestosterone) getTest()));
        }

        return this.serverConfig;
    }

    /**
     * Sets server config that will be used for running tests.
     *
     * @param serverConfig
     */
    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.serverConfig.setTestConfig(this);
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public DbConfig getDbConfig() {
        if (dbConfig == null) {
            this.setDbConfig(Utils.loadConfig(
                    DEFAULT_DB_CONFIG_PROPERTY,
                    DbConfig.class,
                    H2Config.class,
                    (SuperTestosterone) getTest()));

        }

        return this.dbConfig;
    }

    /**
     * Sets db config that will be used for running tests.
     *
     * @param dbConfig
     */
    public void setDbConfig(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
        this.dbConfig.setTestConfig(this);
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public ServletContainerConfig getServletContainerConfig() {
        if (this.servletContainerConfig == null) {
            this.setServletContainerConfig(new ServletContainerConfig(this));
            getServerConfig().setServletContainerConfig(servletContainerConfig);
        }

        return this.servletContainerConfig;
    }

    /**
     * Sets servlet container config that will be used for running test.
     *
     * @param servletContainerConfig
     */
    public void setServletContainerConfig(ServletContainerConfig servletContainerConfig) {
        this.servletContainerConfig = servletContainerConfig;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public CdiConfig getCdiConfig() {
        if (this.cdiConfig == null) {
            this.setCdiConfig(new CdiConfig());
        }

        return this.cdiConfig;
    }

    /**
     * Sets CDI configuration.
     *
     * @param cdiConfig
     */
    public void setCdiConfig(CdiConfig cdiConfig) {
        this.cdiConfig = cdiConfig;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isRunServer() {
        if (this.runServer == null) {
            this.setRunServer(config != null ? config.runServer() : RUN_SERVER);
        }

        return this.runServer;
    }

    /**
     * Sets if server shout be started.
     *
     * @param runServer
     */
    public void setRunServer(Boolean runServer) {
        this.runServer = runServer;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isRunDb() {
        if (this.runDb == null) {
            this.setRunDb(config != null ? config.runDb() : RUN_DB);
        }

        return this.runDb;
    }

    /**
     * Sets if db should be started.
     *
     * @param runDb
     */
    public void setRunDb(Boolean runDb) {
        this.runDb = runDb;
    }

    /**
     * {@inheritDoc }
     *
     * @param method
     * @param test
     * @return
     */
    @Override
    public TestExecutor getTestExecutor(Method method, SuperTestosterone test) {
        return new TestExecutorImpl(method, test);
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public URI getBaseUri() {
        if (this.baseUri == null) {
            String uri = config != null ? config.baseUri() : BASE_URI;
            this.setBaseUri(UriBuilder.fromUri(uri).port(getHttpPort()).build());
        }

        return this.baseUri;
    }

    /**
     * Sets base uri.
     *
     * @param baseUri
     */
    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public int getHttpPort() {
        if (this.httpPort == 0) {
            this.setHttpPort(config != null ? config.httpPort() : HTTP_PORT);
        }

        return this.httpPort;
    }

    /**
     * Sets servers http port.
     *
     * @param httpPort
     */
    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public StartServer getStartServer() {
        if (this.startServer == null) {
            this.setStartServer(config != null ? config.startServer() : StartServer.PER_CLASS);
        }

        return this.startServer;
    }

    /**
     * Sets how server should start, per test class or per test method.
     *
     * @param startServer
     */
    public void setStartServer(StartServer startServer) {
        this.startServer = startServer;
    }

    /**
     * Returns true/false if server should stop after test ends.
     *
     * @return
     */
    public boolean isStopServerAfterTestEnds() {
        if (this.stopServerAfterTestEnds == null) {
            this.setStopServerAfterTestEnds(config != null ? config.stopServerAfterTestEnds() : true);
        }
        return stopServerAfterTestEnds;
    }

    /**
     * Sets flag true/false if server should stop after test ends.
     *
     * @param stopServerAfterTestEnds
     */
    public void setStopServerAfterTestEnds(boolean stopServerAfterTestEnds) {
        this.stopServerAfterTestEnds = stopServerAfterTestEnds;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Setup getSetup() {
        if (this.setup == null) {
            this.setSetup(new Setup(this));
        }
        return this.setup;
    }

    /**
     * Sets setup.
     *
     * @param setup
     */
    public void setSetup(Setup setup) {
        this.setup = setup;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String getMainThreadName() {
        return this.mainThreadName;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Set<Throwable> getExceptions() {
        return exceptions;
    }

    /**
     * {@inheritDoc }
     *
     * @throws Throwable
     */
    @Override
    public void throwExceptions() throws Throwable {
        for (Throwable ex : exceptions) {
            // this will come only to first exception,
            // bot for now it is good enough
            throw ex;
        }
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets if configuration is running or not.
     *
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public Set<Method> getTestMethods() {
        return this.testMethods;
    }

    @Override
    public Set<Method> getExecutedTests() {
        return this.executedTests;
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

        if (!isRunning()) {
            this.testMethods = Utils.getAnnotatedMethods(getTest().getClass(), TestAnnotations.TEST)
                    .stream().filter(p -> p.getDeclaringClass().getName().endsWith("_")
                    && p.getAnnotation(DontIntercept.class) == null
                    && !Utils.isIgnored(p))
                    .collect(Collectors.toSet());

            setRunning(true);
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
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        try {
            if (isRunning() && this.isStopServerAfterTestEnds()) {

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

                        setRunning(false);
                        System.out.println("");

                        int executed = executedTests.size();
                        totalTestsExecuted += executed;

//                        System.out.println(
//                                "\nTests executed: " + executed
//                                + "\nTotal tests executed: " + totalTestsExecuted + "\n\n\n");

                        if (this.getStartServer() == StartServer.PER_TEST_METHOD) {

                            if (executed < 1) {
                                throw new AssertionError("StartServer configuration is set PER_TEST_METHOD but no tests were executed");
                            } else if (executed > 1) {
                                throw new AssertionError("StartServer configuration is set PER_TEST_METHOD but more then 1 test was executed");
                            }
                        } else {
                            if (this.testMethods.size() > 0) {
                                throw new AssertionError("Failed to invoke tests: " + Arrays.toString(this.testMethods.stream()
                                        .map(p -> "\n" + p.getDeclaringClass() + "#" + p.getName())
                                        .collect(Collectors.toList()).stream().toArray(String[]::new)));
                            }
                        }
                    }
                }
            } else if (isRunning() && !this.isStopServerAfterTestEnds()) {
                getServerConfig().stop();
            }
        } finally {
            TestConfigFactory.TEST_CONFIGURATIONS.clear();
        }
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
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
