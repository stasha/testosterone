package info.stasha.testosterone;

import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 *
 * @author stasha
 * @param <T>
 */
public interface TestConfig<T> extends StartStop {

    public static final String BASE_URI = "http://localhost/";
    public static final int HTTP_PORT = 9998;
    public static StartServer START_SERVER = StartServer.PER_CLASS;

    Testosterone getTest();

    ServerConfig getServerConfig();

    ServletContainerConfig getServletContainerConfig();

    DbConfig getDbConfig();

    TestExecutor getTestExecutor(Method method, Testosterone test);

    URI getBaseUri();

    int getHttpPort();

    StartServer getStartServer();

    Setup getSetup();

    String getMainThreadName();

    Set<Throwable> getExceptions();

    void throwExceptions() throws Throwable;

    void init(T root, T dep, List<Testosterone> tests);

}
