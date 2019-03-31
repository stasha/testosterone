package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.cdi.CdiConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * Test configuration.
 *
 * @author stasha
 * @param <T>
 * @param <C>
 */
public interface TestConfig<T, C> extends StartStop {

    public static final String DEFAULT_TEST_CONFIG_PROPERTY = "testosterone.default.test.config";
    public static final String DEFAULT_SERVER_CONFIG_PROPERTY = "testosterone.default.server.config";
    public static final String DEFAULT_DB_CONFIG_PROPERTY = "testosterone.default.db.config";

    public static final String BASE_URI = "http://localhost/";
    public static final int HTTP_PORT = 9998;
    public static StartServer START_SERVER = StartServer.PER_CLASS;
    public static boolean RUN_SERVER = true;
    public static boolean RUN_DB = false;

    /**
     * Returns current running test
     *
     * @return
     */
    T getTest();

    /**
     * Sets current running test
     *
     * @param testosterone
     */
    void setTestosterone(T testosterone);

    /**
     * Sets configuration that will be used for running tests.
     *
     * @param config
     */
    void setConfig(Configuration config);

    /**
     * Returns application.
     *
     * @return
     */
    Application getApplication();

    /**
     * Returns rest client that will be used for running tests.
     *
     * @return
     */
    RestClient getClient();

    /**
     * Returns server config that will be used for running tests.
     *
     * @return
     */
    ServerConfig getServerConfig();

    /**
     * Returns CDI config that will be used for running tests.
     *
     * @return
     */
    CdiConfig getCdiConfig();

    /**
     * Returns true/false if server should be started.
     *
     * @return
     */
    boolean isRunServer();

    /**
     * Returns servlet container config that will be used for running tests.
     *
     * @return
     */
    ServletContainerConfig getServletContainerConfig();

    /**
     * Returns DB config that will be used for running tests
     *
     * @return
     */
    DbConfig getDbConfig();

    /**
     * Returns true/false if DB should be started.
     *
     * @return
     */
    boolean isRunDb();

    /**
     * Returns test executor that will be used for running test.
     *
     * @param method
     * @param test
     * @return
     */
    TestExecutor getTestExecutor(Method method, SuperTestosterone test);

    /**
     * Returns base uri that will be used for running tests.
     *
     * @return
     */
    URI getBaseUri();

    /**
     * Returns server http port that will be used for runnning tests.
     *
     * @return
     */
    int getHttpPort();

    /**
     * Returns how server should run, per test class or per test method.
     *
     * @return
     */
    StartServer getStartServer();

    /**
     * Returns true/false if server should be stopped after test ends.
     *
     * @return
     */
    boolean isStopServerAfterTestEnds();

    /**
     * Returns setup.
     *
     * @return
     */
    Setup getSetup();

    /**
     * Returns main thread name.
     *
     * @return
     */
    String getMainThreadName();

    /**
     * Returns set of exceptions that will be thrown in main thread.
     *
     * @return
     */
    Set<Throwable> getExceptions();

    /**
     * Throws exceptions.
     *
     * @throws Throwable
     */
    void throwExceptions() throws Throwable;

    /**
     * Initialize configuration.
     *
     * @param root
     * @param dep
     * @param tests
     */
    void init(C root, C dep, List<T> tests);

    /**
     * Returns test methods that should be executed.
     *
     * @return
     */
    Set<Method> getTestMethods();

    /**
     * Returns set of executed test methods.
     *
     * @return
     */
    Set<Method> getExecutedTests();

}
