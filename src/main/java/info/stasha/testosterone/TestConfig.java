package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
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

    T getTest();

    void setTestosterone(T testosterone);

    void setConfig(Configuration config);

    Application getApplication();

    RestClient getClient();

    ServerConfig getServerConfig();

    boolean isRunServer();

    ServletContainerConfig getServletContainerConfig();

    DbConfig getDbConfig();

    boolean isRunDb();

    TestExecutor getTestExecutor(Method method, SuperTestosterone test);

    URI getBaseUri();

    int getHttpPort();

    StartServer getStartServer();

    Setup getSetup();

    String getMainThreadName();

    Set<Throwable> getExceptions();

    void throwExceptions() throws Throwable;

    void init(C root, C dep, List<T> tests);

}
