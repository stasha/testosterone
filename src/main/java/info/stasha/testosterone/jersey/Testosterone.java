package info.stasha.testosterone.jersey;

import javax.ws.rs.client.WebTarget;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import info.stasha.testosterone.Utils;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.db.DbConfig;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Testosterone
 *
 * @author stasha
 */
public interface Testosterone {

    static final Logger LOGGER = LoggerFactory.getLogger(Testosterone.class);
    static final Map<String, JerseyTestConfig> TEST_CONFIGURATIONS = new HashMap<>();

    /**
     * Returns testosterone configuration factory.
     *
     * @return
     */
    default JerseyTestConfig getTestConfig() {
        JerseyTestConfig config = TEST_CONFIGURATIONS.get(Utils.getInstrumentedClassName(this));

        // returning existing config
        if (config != null) {
            return config;
        }

        Configuration conf = Testosterone.this.getClass().getAnnotation(Configuration.class);

        try {
            if (conf != null) {
                Constructor con = conf.configuration().
                        getDeclaredConstructor(Testosterone.class, Configuration.class);

                config = (JerseyTestConfig) con.newInstance(this, conf);
            } else {
                config = new JerseyTestConfig(this);
            }

            TEST_CONFIGURATIONS.put(Utils.getInstrumentedClassName(this), config);

            return config;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.error("Failed to create Test Configuration.", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns WebTarget used for sending http requests.
     *
     * @return
     */
    default WebTarget target() {
        return getTestConfig().client.target();
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
     * Override to add mock data to database.
     *
     * @param config
     */
    default void configureMocks(DbConfig config) {

    }

    /**
     * Override to control what SQL query will be executed. Return false to skip
     * executing SQL query.
     *
     * @param queryName
     * @param query
     * @return
     */
    default boolean onDbInit(String queryName, String query) {
        return true;
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
