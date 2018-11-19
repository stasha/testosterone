package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.UriBuilder;

/**
 * Default test configuration.
 *
 * @author stasha
 * @param <T>
 */
public abstract class DefaultTestConfig<T> implements TestConfig<T> {

    private final Set<Throwable> exceptions = new HashSet<>();

    private final Testosterone testosterone;
    private final Configuration config;
    protected ServerConfig serverConfig;
    private ServletContainerConfig servletContainerConfig;
    private DbConfig dbConfig;
    private URI baseUri;
    private int httpPort;
    private StartServer startServer;
    private Setup setup;
    private final String mainThreadName;

    public DefaultTestConfig(Testosterone testosterone) {
        this(testosterone, null);
    }

    public DefaultTestConfig(Testosterone testosterone, Configuration config) {
        this.testosterone = testosterone;
        this.config = config;
        this.mainThreadName = Thread.currentThread().getName();
    }

    @Override
    public Testosterone getTest() {
        return testosterone;
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
    public TestExecutor getTestExecutor(Method method, Testosterone test) {
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
