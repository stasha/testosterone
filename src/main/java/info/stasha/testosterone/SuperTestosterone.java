package info.stasha.testosterone;

import info.stasha.testosterone.cdi.CdiConfig;
import javax.ws.rs.client.WebTarget;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Testosterone
 *
 * @author stasha
 */
public interface SuperTestosterone {

    static final Logger LOGGER = LoggerFactory.getLogger(SuperTestosterone.class);

    /**
     * Returns testosterone configuration factory.
     *
     * @return
     */
    default TestConfig getTestConfig() {
        return TestConfigFactory.getConfig(this);
    }

    /**
     * Returns WebTarget used for sending http requests.
     *
     * @return
     */
    default WebTarget target() {
        return getTestConfig().getClient().target();
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
     * Override to add mock beans.
     *
     * @param config
     */
    default void configureMocks(CdiConfig config) {

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
