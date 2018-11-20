package info.stasha.testosterone.resteasy;

import com.google.inject.AbstractModule;
import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Dependencies;
import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.DefaultTestConfig;
import info.stasha.testosterone.SuperTestosterone;
import info.stasha.testosterone.junit4.AfterClassAnnotation;
import info.stasha.testosterone.junit4.BeforeClassAnnotation;
import info.stasha.testosterone.resteasy.junit4.Testosterone;
import info.stasha.testosterone.servlet.Listener;
import info.stasha.testosterone.servlet.Servlet;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.core.Application;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
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
public class RestEasyTestConfig extends DefaultTestConfig<Testosterone, RestEasyTestConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestEasyTestConfig.class);
    private Application application;

    public RestEasyTestConfig(Testosterone testosterone) {
        super(testosterone);
    }

    public RestEasyTestConfig(Testosterone testosterone, Configuration config) {
        super(testosterone, config);
    }

    /**
     * Returns ResourceConfig.
     *
     * @return
     */
    @Override
    public Application getApplication() {
        if (this.application == null) {
            this.application = RestEasyApplication.getApplication();
        }

        return this.application;
    }

    /**
     * Initializes configuration.
     *
     * @param root
     * @param dep
     * @param tests
     */
    @Override
    public void init(RestEasyTestConfig root, RestEasyTestConfig dep, final List<Testosterone> tests) {

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

//        dep.getTest().configure(root.getApplication());
        dep.getTest().configure(root.getServletContainerConfig());
        final AbstractModule[] configurations = new AbstractModule[1];
        final AbstractModule[] mocks = new AbstractModule[1];
        if (root.equals(dep)) {
            configurations[0] = new AbstractModule() {
                @Override
                public void configure() {
                    root.getTest().configure(this.binder());
                }
            };

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
                    java.util.logging.Logger.getLogger(SuperTestosterone.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        // Configure mocks only for root test. 
        // Skips configuring mocks for classes registered in @Intergration annotation
        if (integration == null || root.equals(dep)) {
            dep.getTest().configureMocks(root.getServletContainerConfig());

            mocks[0] = new AbstractModule() {
                @Override
                protected void configure() {
                    dep.getTest().configureMocks(this.binder());
                }
            };

        }

        tests.add(dep.getTest());

        if (integration != null || dependencies != null) {
            List<Class<? extends SuperTestosterone>> testClasses = null;

            if (integration != null && root.equals(dep)) {
                testClasses = Arrays.asList(integration.value());
                LOGGER.info("Gathering integration configurations");
            } else if (dependencies != null) {
                testClasses = Arrays.asList(dependencies.value());
                LOGGER.info("Gathering dependency configurations");
            }

            if (testClasses != null) {

                Collections.reverse(testClasses);

                for (Class<? extends SuperTestosterone> cls : testClasses) {
                    try {
                        SuperTestosterone t = TestInstrumentation.testClass(cls, new BeforeClassAnnotation(), new AfterClassAnnotation()).newInstance();
                        t.getTestConfig().init(root, t.getTestConfig(), tests);
                    } catch (InstantiationException | IllegalAccessException ex) {
                        LOGGER.error("Failed to create SuperTestosterone instance", ex);
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

            final AbstractModule resource = new AbstractModule() {
                @Override
                protected void configure() {
                    this.bind(root.getTest().getClass());
                }
            };
            
            RestEasyApplication.test = root.getTest();

            LOGGER.info("Adding test to resources: {}", root.getTest());
            getApplication().getClasses().add(root.getTest().getClass());
//                    getApplication().getSingletons().add(root.getSetup());

            final AbstractModule testmodules = new AbstractModule() {
                @Override
                protected void configure() {
                    tests.forEach((test) -> {
                        test.configure(this.binder());
                        getApplication().getClasses().add(test.getClass());
                    });
                }
            };

//            Guice.createInjector(configurations, mocks, resource, testmodules);
            LOGGER.info("Configuration end:   {}", root.getTest().getClass().getName());
//                    root.getServerConfig().setConfigurationObject(root.getApplication());

//            root.getServletContainerConfig().addFilter(new Filter(GuiceFilter.class, "/*"));
//            root.getServletContainerConfig().addListener(new Listener(new GuiceServletContextListener() {
//                @Override
//                protected Injector getInjector() {
//                    return Guice.createInjector(new ServletModule() {
//                        @Override
//                        protected void configureServlets() {
//                            serve("/*").with(new HttpServletDispatcher());
//
//                            bind(root.getTest().getClass());
//                            bind(ServerConfig.class).to(JettyServerConfig.class);
//                        }
//
//                    }, configurations[0], mocks[0], resource, testmodules);
//                }
//            }));
//            root.getServletContainerConfig().addContextParam("javax.ws.rs.Application",
//                    "info.stasha.testosterone.resteasy.RestEasyApplication");
            root.getServletContainerConfig().addContextParam("resteasy.guice.modules",
                    "info.stasha.testosterone.resteasy.RestEasyApplication");
            root.getServletContainerConfig().addServlet(new Servlet(HttpServletDispatcher.class, "/*"));
            root.getServletContainerConfig().addListener(new Listener(GuiceResteasyBootstrapServletContextListener.class));
            
        }

    }

    boolean started;

    /**
     * Starts server.
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

        if (!started && !getServerConfig().isRunning()) {
            started = true;
            init(this, this, new ArrayList<>());

            super.start();
        }
    }

}
