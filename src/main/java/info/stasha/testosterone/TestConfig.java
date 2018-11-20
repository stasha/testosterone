package info.stasha.testosterone;

import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author stasha
 * @param <C>
 */
public interface TestConfig<T, C> extends StartStop {

    public static final String BASE_URI = "http://localhost/";
    public static final int HTTP_PORT = 9998;
    public static StartServer START_SERVER = StartServer.PER_CLASS;

    T getTest();

    Application getApplication();

    RestClient getClient();

    ServerConfig getServerConfig();

    ServletContainerConfig getServletContainerConfig();

    DbConfig getDbConfig();

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
