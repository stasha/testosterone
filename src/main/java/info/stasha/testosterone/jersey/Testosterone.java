package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Setup;
import javax.ws.rs.client.WebTarget;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.ConfigFactory;

/**
 * Interface that should be implemented by every JUnit4 test class that needs
 * Testosterone functionality.
 *
 * @author stasha
 */
public interface Testosterone {
	
	/**
	 * Returns testosterone configuration factory.
	 *
	 * @return
	 */
	default ConfigFactory getConfigFactory() {
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
	default ServerConfig getConfiguration() {
		return getSetup().getConfiguration();
	}

	/**
	 * Returns WebTarget used for sending http requests.
	 *
	 * @return
	 */
	default WebTarget target() {
		return getConfiguration().target();
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
	 * Initializes configuration.
	 *
	 * @param config
	 */
	default void initConfiguration(ServerConfig config) {
		// configure ResourceConfig
		configure(config.getResourceConfig());
		// configure Servlet container
		configure(config.getServletContainerConfig());
		// configure ResourceConfig + servlet container
		configure(config.getResourceConfig(), config.getServletContainerConfig());
		// configure AbstractBinder
		config.getResourceConfig().register(new AbstractBinder() {
			@Override
			protected void configure() {
				// invokes method for configuring AbstractBinder
				Testosterone.this.configure(this);
			}
		});
		// initializes configuration
		config.initConfiguration(this);
	}

	/**
	 * Starts server.
	 *
	 * @throws Exception
	 */
	default void start() throws Exception {
		if (!getConfiguration().isRunning()) {
			System.out.println("");
			// initializes configuration
			initConfiguration(getConfiguration());
			// runs before server start method
			beforeServerStart();
			// starts server
			getConfiguration().start();
			// runs after server start method
			afterServerStart();
		}
	}

	/**
	 * Stops server.
	 *
	 * @throws Exception
	 */
	default void stop() throws Exception {
		if (getConfiguration().isRunning()) {
			// runs before server stop method
			beforeServerStop();
			// stops server
			getConfiguration().stop();
			// runs after server stop method
			afterServerStop();
			System.out.println("");
		}
	}

	/**
	 * Override to configure ResourceConfig.
	 *
	 * @param config
	 */
	default void configure(ResourceConfig config) {

	}

	/**
	 * Override to configure AbstractBinder.
	 *
	 * @param binder
	 */
	default void configure(AbstractBinder binder) {

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
	 * Override to configure ResourceConfig including servlet container.
	 *
	 * @param config
	 * @param servletConfig
	 */
	default void configure(ResourceConfig config, ServletContainerConfig servletConfig) {

	}

	/**
	 * Override this method if you need to do stuff before server starts.
	 */
	default void beforeServerStart() {

	}

	/**
	 * Override this method if you need to do stuff immediately after server
	 * starts.
	 */
	default void afterServerStart() {

	}

	/**
	 * Override this method if you need to do stuff before server stops.
	 */
	default void beforeServerStop() {

	}

	/**
	 * Override this method if you need to do stuff immediately after server
	 * stops.
	 */
	default void afterServerStop() {

	}

}
