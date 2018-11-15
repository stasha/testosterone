package info.stasha.testosterone.jersey;

import info.stasha.testosterone.jersey.inject.InjectTestResolver;
import info.stasha.testosterone.jersey.inject.ValueInjectionResolver;
import info.stasha.testosterone.Setup;
import javax.ws.rs.client.WebTarget;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.ConfigFactory;
import info.stasha.testosterone.Utils;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.InjectTest;
import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.annotation.Value;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2ConnectionFactory;
import info.stasha.testosterone.jersey.inject.MockInjectionResolver;
import info.stasha.testosterone.jersey.inject.SpyInjectionResolver;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.inject.Singleton;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.mockito.AdditionalAnswers.delegatesTo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface that should be implemented by every JUnit4 test class that needs
 * Testosterone functionality.
 *
 * @author stasha
 */
public interface Testosterone {

    static final Logger LOGGER = LoggerFactory.getLogger(Testosterone.class);

    /**
     * Returns testosterone configuration factory.
     *
     * @return
     */
    default ConfigFactory getConfigFactory() {
        Configuration conf = Testosterone.this.getClass().getAnnotation(Configuration.class);
        if (conf != null && conf.configuration() != null) {
            try {
//				LOGGER.info("Creating ConfigFactory {} from @Configuration annotation.", conf.configuration().getName());
                return conf.configuration().newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.error("Failed to create configuration from @Configuration annotation.");
            }
        }

//		LOGGER.info("Creating default JettyConfigFactory");
        return new JettyConfigFactory();
    }

    /**
     * Returns testosterone setup.
     *
     * @return
     */
    default Setup getSetup() {
        return getConfigFactory().getSetup(this);
    }

    /**
     * Returns testosterone configuration.
     *
     * @return
     */
    default ServerConfig getServerConfig() {
        return getSetup().getServerConfig();
    }

    /**
     * Returns WebTarget used for sending http requests.
     *
     * @return
     */
    default WebTarget target() {
        return getServerConfig().target();
    }

    /**
     * Returns WebTarget used for sending http requests.
     *
     * @param path
     * @return
     */
    default WebTarget target(String path) {
        return target().path(path);
    }

    default Map<String, Testosterone> getTests() {
        return getSetup().getTests();
    }

    /**
     * Initializes configuration.
     *
     * @param config
     */
    default void initConfiguration(ServerConfig config) {

//        System.out.println(this.getClass().getName());
        for (Field f : this.getClass().getSuperclass().getDeclaredFields()) {
            f.setAccessible(true);
//            System.out.println(f.getName() + " : " + Arrays.toString(f.getAnnotations()));
            Mock m = f.getAnnotation(Mock.class);
            Spy s = f.getAnnotation(Spy.class);

            try {
                Object obj = f.get(this);

                if (m != null && obj != null) {
                    f.set(this, Mockito.mock(f.getType(), m.answer()));
                } else if (s != null && obj != null) {
                    f.set(this, Mockito.mock(obj.getClass(), delegatesTo(obj)));
                }

            } catch (IllegalArgumentException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(Testosterone.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // configureMocks ResourceConfig
        configure(config.getResourceConfig());
        // configure Servlet container
        configure(config.getServletContainerConfig());
        // configure database 
//        configure()
        // configureMocks AbstractBinder
        config.getResourceConfig().register(new AbstractBinder() {
            @Override
            protected void configure() {
                this.bindFactory(new Factory<Testosterone>() {
                    @Override
                    public Testosterone provide() {
                        return Testosterone.this;
                    }

                    @Override
                    public void dispose(Testosterone instance) {
                        // do nothing
                    }
                }).to(Testosterone.class).in(Singleton.class);
                // H2 connection factory
                this.bindFactory(H2ConnectionFactory.class)
                        .to(Connection.class)
                        .in(RequestScoped.class)
                        .proxy(true)
                        .proxyForSameScope(false);

                // custom @Value injection resolver
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

                // invokes method for configuring AbstractBinder
                Testosterone.this.configure(this);
            }
        });

        // Mocked configuratinos are excluded from integration tests
        Integration integration = getSetup().getIntegration();

        // if this is integration, then build configuration from all 
        // registered classes in @Integration annotation
        if (integration != null && getSetup().getRoot() == null) {

            List<Class<? extends Testosterone>> testClasses = Arrays.asList(integration.value());
            Collections.reverse(testClasses);

            Map<String, Testosterone> tests = new LinkedHashMap();
            for (Class<? extends Testosterone> cls : testClasses) {
                try {
                    Testosterone t = cls.newInstance();
                    t.getSetup().setParent(getSetup());
                    t.getSetup().setRoot(getSetup());
                    t.initConfiguration(getServerConfig());
                    tests.put(cls.getTypeName(), t);
                } catch (InstantiationException | IllegalAccessException ex) {
                    java.util.logging.Logger.getLogger(Testosterone.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            getSetup().setTests(tests);

            config.getResourceConfig().property("tests", tests);

            config.getResourceConfig().register(new AbstractBinder() {
                @Override
                protected void configure() {
                    tests.forEach((k, v) -> {
                        v.configure(this);
                    });
                }
            });
        }

        if (integration == null) {
            // configureMocks MockingResourceConfig
            configureMocks(config.getResourceConfig());
            // configuring MockingServletContainerConfig
            configureMocks(config.getServletContainerConfig());
            // configureMocks mocking abstract binder
            config.getResourceConfig().register(new AbstractBinder() {
                @Override
                protected void configure() {
                    Testosterone.this.configureMocks(this);
                }
            });
        }

        // invoking only for root setup
        if (getSetup().getRoot() == null) {
            config.getResourceConfig().property("test", this);
            // registering setup so it can listen for application events
            config.getResourceConfig().register(getSetup());
            // registering db config so db is started/stopped with jersey application
            config.getResourceConfig().register(getSetup().getDbConfig());
//            config.getResourceConfig().property("com.sun.jersey.api.json.POJOMappingFeature", true);
            // initializes configuration
            config.initConfiguration(this);
        }

    }

    /**
     * Starts server.
     *
     * @throws Exception
     */
    default void start() throws Exception {
        if (!getServerConfig().isRunning()) {
            // runs before server start method
            getSetup().beforeServerStart(this);
            // initializes configuration
            initConfiguration(getServerConfig());
            // starts server
            getServerConfig().start();

            // Invoke afterServerStart only if resource is singleton.
            // If there is no Singleton annotation, afterServerStart is 
            // invoked by @PostConstruct interceptor
            if (Utils.isAnnotationPresent(this, Singleton.class)) {
                getSetup().afterServerStart(this);
            }
        }
    }

    /**
     * Stops server.
     *
     * @throws Exception
     */
    default void stop() throws Exception {
        if (getServerConfig().isRunning()) {
            // runs before server stop method
            getSetup().beforeServerStop(this);
            // stops server
            getServerConfig().stop();
            // runs after server stop method
            getSetup().afterServerStop(this);
            getSetup().clearFlags();

            System.out.println("");
        }
    }

    /**
     * Override to configureMocks ResourceConfig.
     *
     * @param config
     */
    default void configure(ResourceConfig config) {

    }

    /**
     * Override to configureMocks ResourceConfig.
     *
     * @param config
     */
    default void configureMocks(ResourceConfig config) {

    }

    /**
     * Override to configureMocks AbstractBinder.
     *
     * @param binder
     */
    default void configure(AbstractBinder binder) {

    }

    /**
     * Override to configureMocks MockingAbstractBinder.<br>
     * This method is not invoked when Test class is @Integration.
     *
     * @param binder
     */
    default void configureMocks(AbstractBinder binder) {

    }

    /**
     * Override to configure servlet container. Register servlets, filters,
     * listeners and context params.
     *
     * @param config
     */
    default void configure(ServletContainerConfig config) {

    }

    /**
     * Override to configure servlet container. Register servlets, filters,
     * listeners and context params.
     *
     * @param config
     */
    default void configureMocks(ServletContainerConfig config) {

    }

    /**
     * Override to configure database.
     *
     * @param config
     */
    default void configure(DbConfig config) {

    }

    /**
     *
     * @param config
     */
    default void configureMocks(DbConfig config) {

    }

    /**
     * Override this method if you need to do stuff before server starts.
     *
     * @throws java.lang.Exception
     */
    default void beforeServerStart() throws Exception {

    }

    /**
     * Override this method if you need to do stuff immediately after server
     * starts.<br>
     * Note that in this point object is fully constructed including injections.
     *
     * @throws java.lang.Exception
     */
    default void afterServerStart() throws Exception {

    }

    /**
     * Override this method if you need to do stuff before server stops.
     *
     * @throws java.lang.Exception
     */
    default void beforeServerStop() throws Exception {

    }

    /**
     * Override this method if you need to do stuff immediately after server
     * stops.
     *
     * @throws java.lang.Exception
     */
    default void afterServerStop() throws Exception {

    }

}
