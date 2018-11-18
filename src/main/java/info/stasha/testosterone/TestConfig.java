package info.stasha.testosterone;

import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 *
 * @author stasha
 */
public interface TestConfig {

    public static final String BASE_URI = "http://localhost/";
    public static final int HTTP_PORT = 9998;
    public static StartServer START_SERVER = StartServer.PER_CLASS;

    Testosterone getTest();

    ServerConfig getServerConfig();

    ServletContainerConfig getServletContainerConfig();

    DbConfig getDbConfig();

    TestExecutor getTestExecutor(Method method, Testosterone test);

    String getBaseUri();

    int getHttpPort();

    StartServer getStartServer();

    Setup getSetup();

    String getMainThreadName();

    Set<Throwable> getExceptions();

    void throwExceptions() throws Throwable;

    void init(TestConfig root, TestConfig dep, List<Testosterone> tests);

    void start() throws Exception;

    void stop() throws Exception;

}
