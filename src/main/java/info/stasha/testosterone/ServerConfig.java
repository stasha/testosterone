package info.stasha.testosterone;

import info.stasha.testosterone.servlet.ServletContainerConfig;

/**
 * Testosterone configuration interface.
 *
 * @author stasha
 * @param <T>
 */
public interface ServerConfig<T> extends TestConfigBase, StartStop {

    /**
     * Returns servlet configuration object where context params, listeners,
     * filters and servlets can be registered.
     *
     * @return
     */
    ServletContainerConfig getServletContainerConfig();

    /**
     * Sets servlet container config.
     *
     * @param config
     */
    void setServletContainerConfig(ServletContainerConfig config);

    /**
     * Any object needed for configuring and running application on the server.
     *
     * @param configuration
     */
    void setConfigurationObject(T configuration);

}
