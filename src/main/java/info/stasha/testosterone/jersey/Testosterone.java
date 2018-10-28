package info.stasha.testosterone.jersey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import info.stasha.testosterone.servlet.ServletContainerConfig;

/**
 *
 * @author stasha
 */

public interface Testosterone {

	Logger LOGGER = Logger.getLogger(Testosterone.class.getName());

	Map<Class<?>, JerseyConfiguration> CONFIGURATION = new HashMap<>();

	default Set<Throwable> getMessages() {
		return getConfiguration().getMessages();
	}

	default List<Throwable> getExpectedExceptions() {
		return getConfiguration().getExpectedExceptions();
	}

	default WebTarget target() {
		return getConfiguration().target();
	}

	default WebTarget target(String path) {
		return target().path(path);
	}

	default Client client() {
		return getConfiguration().client();
	}

	default JerseyConfiguration getConfiguration() {
		if (!CONFIGURATION.containsKey(this.getClass())) {
			try {
				info.stasha.testosterone.annotation.Configuration c
						= this.getClass().getAnnotation(info.stasha.testosterone.annotation.Configuration.class);
				JerseyConfiguration conf;
				if (c != null) {
					conf = (JerseyConfiguration) c.value().newInstance();
				} else {
					conf = new JettyConfiguration();
				}
				CONFIGURATION.put(this.getClass(), conf);
			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(Testosterone.class.getName()).log(Level.SEVERE, null, ex);
				throw new RuntimeException(ex);
			}
		}
		return CONFIGURATION.get(this.getClass());
	}

	default void configure(ResourceConfig config) {

	}

	default void configure(AbstractBinder binder) {

	}

	default void configure(ResourceConfig config, AbstractBinder binder) {

	}

	default void configure(ServletContainerConfig config) {

	}

	default void configure(ResourceConfig config, ServletContainerConfig servletConfig) {

	}

	default void configure(AbstractBinder binder, ServletContainerConfig servletConfig) {

	}

	default void configure(ResourceConfig config, AbstractBinder binder, ServletContainerConfig servletConfig) {

	}

	default void start() throws Exception {
		if (!getConfiguration().isRunning()) {
			System.out.println("");
			getConfiguration().stop();
			
			configure(getConfiguration().getResourceConfig());
			configure(getConfiguration().getAbstractBinder());
			
			configure(getConfiguration().getResourceConfig(),
					getConfiguration().getAbstractBinder());
			
			configure(getConfiguration().getServletContainerConfig());
			
			configure(getConfiguration().getResourceConfig(),
					getConfiguration().getServletContainerConfig());
			
			configure(getConfiguration().getAbstractBinder(),
					getConfiguration().getServletContainerConfig());
			
			configure(getConfiguration().getResourceConfig(),
					getConfiguration().getAbstractBinder(),
					getConfiguration().getServletContainerConfig());
			
			getConfiguration().init(this);
			getConfiguration().start();
		}
	}

	default void stop() throws Exception {
		if (getConfiguration().isRunning()) {
			getConfiguration().stop();
			System.out.println("");
		}
	}

	default void beforeTest() throws Exception {
		start();
	}

	default void afterTest() throws Exception {
		stop();

	}

}
