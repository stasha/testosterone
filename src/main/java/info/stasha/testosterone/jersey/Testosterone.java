package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Setup;
import javax.ws.rs.client.WebTarget;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.ConfigFactory;
import info.stasha.testosterone.Utils;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.db.H2ConnectionFactory;
import java.sql.Connection;
import javax.inject.Singleton;
import org.glassfish.jersey.process.internal.RequestScoped;
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
				//
				this.bindFactory(H2ConnectionFactory.class, Singleton.class)
						.to(Connection.class)
						.in(RequestScoped.class)
						.proxy(true)
						.proxyForSameScope(false);

				// invokes method for configuring AbstractBinder
				Testosterone.this.configure(this);
			}
		});
		// registering setup so it can listen for application events
		config.getResourceConfig().register(getSetup());
		// registering db config so db is started/stopped with jersey application
		config.getResourceConfig().register(getSetup().getDbConfig());
		// initializes configuration
		config.initConfiguration(this);
	}

	/**
	 * Starts server.
	 *
	 * @throws Exception
	 */
	default void start() throws Exception {
		if (!getServerConfig().isRunning()) {
			// initializes configuration
			initConfiguration(getServerConfig());
			// runs before server start method
			getSetup().beforeServerStart(this);
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
