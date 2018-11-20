package info.stasha.testosterone.jersey;

import info.stasha.testosterone.servers.JettyServerConfig;
import info.stasha.testosterone.TestInExecution;
import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.Utils;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Dependencies;
import info.stasha.testosterone.annotation.InjectTest;
import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.annotation.LoadFile;
import info.stasha.testosterone.annotation.Value;
import info.stasha.testosterone.DefaultTestConfig;
import info.stasha.testosterone.TestConfigFactory;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.jersey.inject.InjectTestResolver;
import info.stasha.testosterone.jersey.inject.InputStreamInjectionResolver;
import info.stasha.testosterone.jersey.inject.MockInjectionResolver;
import info.stasha.testosterone.jersey.inject.SpyInjectionResolver;
import info.stasha.testosterone.jersey.inject.ValueInjectionResolver;
import info.stasha.testosterone.junit4.AfterClassAnnotation;
import info.stasha.testosterone.junit4.BeforeClassAnnotation;
import info.stasha.testosterone.servlet.Servlet;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Singleton;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import static org.mockito.AdditionalAnswers.delegatesTo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jersey test configuration.
 *
 * @author stasha
 */
public class JerseyTestConfig extends DefaultTestConfig<JerseyTestConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyTestConfig.class);
    private final RestClient client = new RestClient(this);
    private ResourceConfig resourceConfig;

    public JerseyTestConfig(Testosterone testosterone) {
        super(testosterone);
    }

    public JerseyTestConfig(Testosterone testosterone, Configuration config) {
        super(testosterone, config);
    }

    @Override
    public JettyServerConfig getServerConfig() {
        if (serverConfig == null) {
            this.serverConfig = new JettyServerConfig(this);
        }

        return (JettyServerConfig) this.serverConfig;
    }

    @Override
    public RestClient getClient() {
        return this.client;
    }

    /**
     * Returns ResourceConfig.
     *
     * @return
     */
    @Override
    public ResourceConfig getApplication() {
        if (this.resourceConfig == null) {
            this.resourceConfig = new ResourceConfig();
        }

        return this.resourceConfig;
    }

    /**
     * Initializes configuration.
     *
     * @param root
     * @param dep
     * @param tests
     */
    @Override
    public void init(JerseyTestConfig root, JerseyTestConfig dep, final List<Testosterone> tests) {

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

        dep.getTest().configure(root.getApplication());
        dep.getTest().configure(root.getServletContainerConfig());

        if (root.equals(dep)) {
            root.getApplication().register(new AbstractBinder() {

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
            dep.getTest().configureMocks(root.getApplication());
            dep.getTest().configureMocks(root.getServletContainerConfig());

            root.getApplication().register(new AbstractBinder() {
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
                root.getApplication().register(root.getTest());
            } else {
                LOGGER.info("Adding test to resources: {}", root.getTest());
                root.getApplication().register(root.getTest().getClass());
            }

            root.getApplication().register(InputStreamInjectionResolver.class);

            root.getApplication().register(new AbstractBinder() {

                @Override
                protected void configure() {
                    tests.forEach((test) -> {
                        test.configure(this);
                    });
                }
            });
            // registering setup so it can listen for application events
            root.getApplication().register(root.getSetup());

            // registering jersey servlet
            Servlet s = new Servlet(new ServletContainer(root.getApplication()),
                    root.getServletContainerConfig().getJerseyServletPath()).setInitOrder(1);
            root.getServletContainerConfig().addServlet(s);

            LOGGER.info("Configuration end:   {}", root.getTest().getClass().getName());
            root.getServerConfig().setConfigurationObject(root.getApplication());
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

            getSetup().beforeServerStart(getTest());

            LOGGER.info("Starting server with {} configuration", getStartServer());

            getDbConfig().start();

            client.start();

            getServerConfig().start();

            LOGGER.info(this.toString());
            // Invoke afterServerStart only if resource is singleton.
            // If there is no Singleton annotation, afterServerStart is 
            // invoked by @PostConstruct interceptor
            if (Utils.isAnnotationPresent(getTest(), Singleton.class)) {
                getSetup().afterServerStart(getTest());
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
                client.stop();

                getSetup().beforeServerStop(getTest());

                LOGGER.info("Stopping server configured with: {}", getStartServer());
                getServerConfig().stop();
                getDbConfig().stop();
                getSetup().afterServerStop(getTest());
                getSetup().clearFlags();

                System.out.println("");
            }
        } finally {
            TestConfigFactory.TEST_CONFIGURATIONS.remove(Utils.getInstrumentedClassName(getTest()));
        }
    }

    @Override
    public boolean isRunning() {
        return getServerConfig().isRunning();
    }

}
