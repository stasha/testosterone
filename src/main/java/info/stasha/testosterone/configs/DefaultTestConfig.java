package info.stasha.testosterone.configs;

import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.Setup;
import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.TestExecutor;
import info.stasha.testosterone.TestExecutorImpl;
import info.stasha.testosterone.TestInExecution;
import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.Utils;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Dependencies;
import info.stasha.testosterone.annotation.InjectTest;
import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.annotation.LoadFile;
import info.stasha.testosterone.annotation.Value;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.jersey.JettyServerConfig;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.jersey.inject.InjectTestResolver;
import info.stasha.testosterone.jersey.inject.InputStreamInjectionResolver;
import info.stasha.testosterone.jersey.inject.MockInjectionResolver;
import info.stasha.testosterone.jersey.inject.SpyInjectionResolver;
import info.stasha.testosterone.jersey.inject.ValueInjectionResolver;
import info.stasha.testosterone.junit4.AfterClassAnnotation;
import info.stasha.testosterone.junit4.BeforeClassAnnotation;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.inject.Singleton;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.mockito.AdditionalAnswers.delegatesTo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default test configuration.
 *
 * @author stasha
 */
public class DefaultTestConfig implements TestConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTestConfig.class);

    private final Set<Throwable> exceptions = new HashSet<>();

    private final Testosterone testosterone;
    private final Configuration config;
    private ServerConfig serverConfig;
    private ServletContainerConfig servletContainerConfig;
    private DbConfig dbConfig;
    private String baseUri;
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
    public ServerConfig getServerConfig() {
        if (serverConfig == null) {
            this.serverConfig = new JettyServerConfig(this);
        }

        return this.serverConfig;
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
    public String getBaseUri() {
        if (this.baseUri == null) {
            this.baseUri = config != null ? config.baseUri() : BASE_URI;
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
     * Initializes configuration.
     *
     * @param root
     * @param dep
     * @param tests
     */
    @Override
    public void init(TestConfig root, TestConfig dep, final List<Testosterone> tests) {

        //The configuration order is:
        //
        // 1. For Jersey bindings, first registered is one that gets used.
        //    This means root configuration must be processed before other
        //    registered configurations in @Dependencies or @Integration. 
        //    This allows overriding bindings specified 
        //    in @Dependencies and @Integration.
        //
        // 2. For DB, root configuration must be processed as last, 
        //
        if (root.equals(dep)) {
            LOGGER.info("Configuration start: {}", dep.getTest().getClass().getName());
        } else {
            LOGGER.info("Processing dependency: {}", dep.getTest().getClass().getName());
        }

        Integration integration = root.getTest().getClass().getAnnotation(Integration.class);
        Dependencies dependencies = dep.getTest().getClass().getAnnotation(Dependencies.class);

        dep.getTest().configure(root.getServerConfig().getResourceConfig());
        dep.getTest().configure(root.getServletContainerConfig());

        if (root.equals(dep)) {
            root.getServerConfig().getResourceConfig().register(new AbstractBinder() {

                @Override
                protected void configure() {

                    this.bindFactory(new Factory<Testosterone>() {

                        @Override
                        public Testosterone provide() {
                            return root.getTest();
                        }

                        @Override
                        public void dispose(Testosterone instance) {
                        }
                    }).to(Testosterone.class).in(Singleton.class);

                    this.bindFactory(root.getDbConfig().getConnectionFactory())
                            .to(Connection.class)
                            .in(RequestScoped.class)
                            .proxy(true)
                            .proxyForSameScope(false);

                    this.bindFactory(new Factory<DbConfig>() {
                        @Override
                        public DbConfig provide() {
                            return root.getDbConfig();
                        }

                        @Override
                        public void dispose(DbConfig instance) {
                        }
                    }).to(DbConfig.class).in(Singleton.class);

                    this.bindFactory(new Factory<TestInExecution>() {
                        @Override
                        public TestInExecution provide() {
                            return root.getSetup().getTestInExecution();
                        }

                        @Override
                        public void dispose(TestInExecution instance) {
                        }
                    }).to(TestInExecution.class).in(RequestScoped.class);

                    // injection resolvers
                    this.bind(ValueInjectionResolver.class)
                            .to(new TypeLiteral<InjectionResolver<Value>>() {
                            }).in(Singleton.class);
                    this.bind(InjectTestResolver.class)
                            .to(new TypeLiteral<InjectionResolver<InjectTest>>() {
                            }).in(Singleton.class);
                    this.bind(MockInjectionResolver.class)
                            .to(new TypeLiteral<InjectionResolver<Mock>>() {
                            }).in(Singleton.class);
                    this.bind(SpyInjectionResolver.class)
                            .to(new TypeLiteral<InjectionResolver<Spy>>() {
                            }).in(Singleton.class);
                    this.bind(InputStreamInjectionResolver.class)
                            .to(new TypeLiteral<InjectionResolver<LoadFile>>() {
                            }).in(Singleton.class);

                    root.getTest().configure(this);
                }
            });

            for (Field f : root.getTest().getClass().getSuperclass().getDeclaredFields()) {
                f.setAccessible(true);
                Mock m = f.getAnnotation(Mock.class);
                Spy s = f.getAnnotation(Spy.class);

                try {
                    Object obj = f.get(root.getTest());

                    if (m != null && obj != null) {
                        f.set(root.getTest(), Mockito.mock(f.getType(), m.answer()));
                    } else if (s != null && obj != null) {
                        f.set(root.getTest(), Mockito.mock(obj.getClass(), delegatesTo(obj)));
                    }

                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    java.util.logging.Logger.getLogger(Testosterone.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        // Configure mocks only for root test. 
        // Skips configuring mocks for classes registered in @Intergration annotation
        if (integration == null || root.equals(dep)) {
            dep.getTest().configureMocks(root.getServerConfig().getResourceConfig());
            dep.getTest().configureMocks(root.getServletContainerConfig());

            root.getServerConfig().getResourceConfig().register(new AbstractBinder() {
                @Override
                protected void configure() {
                    dep.getTest().configureMocks(this);
                }
            });
        }

        tests.add(dep.getTest());

        if (integration != null || dependencies != null) {
            List<Class<? extends Testosterone>> testClasses = null;

            if (integration != null && root.equals(dep)) {
                testClasses = Arrays.asList(integration.value());
                LOGGER.info("Gathering integration configurations");
            } else if (dependencies != null) {
                testClasses = Arrays.asList(dependencies.value());
                LOGGER.info("Gathering dependency configurations");
            }

            if (testClasses != null) {

                Collections.reverse(testClasses);

                for (Class<? extends Testosterone> cls : testClasses) {
                    try {
                        Testosterone t = TestInstrumentation.testClass(cls, new BeforeClassAnnotation(), new AfterClassAnnotation()).newInstance();
                        t.getTestConfig().init(root, t.getTestConfig(), tests);
                    } catch (InstantiationException | IllegalAccessException ex) {
                        LOGGER.error("Failed to create Testosterone instance", ex);
                        throw new RuntimeException(ex);
                    }
                }
            }

        }

        // For reason why DB configuration is in the end, please see 
        // the comments about order at the beginning of this method.
        dep.getTest().configure(root.getDbConfig());
        // Configure mocks only for root test. 
        // Skips configuring mocks for classes registered in @Intergration annotation
        if (integration == null || root.equals(dep)) {
            dep.getTest().configureMocks(root.getDbConfig());
        }

        // After everything is configured, including @Dependencies na @Integration,
        // we initialize the server
        if (root.equals(dep)) {
            if (Utils.isAnnotationPresent(root.getTest(), Singleton.class)) {
                LOGGER.info("Adding test as Singleton to resources: {}", root.getTest());
                root.getServerConfig().getResourceConfig().register(root.getTest());
            } else {
                LOGGER.info("Adding test to resources: {}", root.getTest());
                this.getServerConfig().getResourceConfig().register(root.getTest().getClass());
            }
            
            root.getServerConfig().getResourceConfig().register(InputStreamInjectionResolver.class);

            root.getServerConfig().getResourceConfig().register(new AbstractBinder() {

                @Override
                protected void configure() {
                    tests.forEach((test) -> {
                        test.configure(this);
                    });
                }
            });
            // registering setup so it can listen for application events
            root.getServerConfig().getResourceConfig().register(root.getSetup());

            LOGGER.info("Configuration end:   {}", root.getTest().getClass().getName());
            root.getServerConfig().init();
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
            init(this, this, new ArrayList<>());

            setup.beforeServerStart(this.testosterone);

            LOGGER.info("Starting server with {} configuration", getStartServer());

            dbConfig.start();
            serverConfig.start();

            LOGGER.info(this.toString());
            // Invoke afterServerStart only if resource is singleton.
            // If there is no Singleton annotation, afterServerStart is 
            // invoked by @PostConstruct interceptor
            if (Utils.isAnnotationPresent(this.testosterone, Singleton.class)) {
                setup.afterServerStart(this.testosterone);
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
                setup.beforeServerStop(this.testosterone);

                LOGGER.info("Stopping server configured with: {}", getStartServer());
                serverConfig.stop();
                dbConfig.stop();
                setup.afterServerStop(this.testosterone);
                setup.clearFlags();

                System.out.println("");
            }
        } finally {
            Testosterone.TEST_CONFIGURATIONS.remove(Utils.getInstrumentedClassName(this.testosterone));
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
