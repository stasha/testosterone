package info.stasha.testosterone.jersey;

import info.stasha.testosterone.TestosteroneConfig;
import info.stasha.testosterone.TestosteroneConfigFactory;
import info.stasha.testosterone.TestosteroneSetup;
import java.util.logging.Logger;
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

	default TestosteroneConfigFactory getConfigFactory() {
		return new JettyConfigFactory();
	}

	default TestosteroneSetup getSetup() {
		return getConfigFactory().getSetup(this);
	}

	default TestosteroneConfig getConfiguration() {
		return getSetup().getConfiguration();
	}

	default WebTarget target() {
		return getConfiguration().target();
	}

	default WebTarget target(String path) {
		return target().path(path);
	}

	default void initConfiguration(TestosteroneConfig config) {
		configure(config.getResourceConfig());
		config.getResourceConfig().register(new AbstractBinder() {
			@Override
			protected void configure() {
				Testosterone.this.configure(this);
			}
		});

		configure(config.getServletContainerConfig());
		configure(config.getResourceConfig(), config.getServletContainerConfig());
		config.initConfiguration(this);
	}

	default void start() throws Exception {
		if (!getConfiguration().isRunning()) {
			System.out.println("");
			initConfiguration(getConfiguration());
			beforeServerStart();
			getConfiguration().start();
			afterServerStart();
		}
	}

	default void stop() throws Exception {
		if (getConfiguration().isRunning()) {
			beforeServerStop();
			getConfiguration().stop();
			afterServerStop();
			System.out.println("");
		}
	}

	default void configure(ResourceConfig config) {

	}

	default void configure(AbstractBinder binder) {

	}

	default void configure(ServletContainerConfig config) {

	}

	default void configure(ResourceConfig config, ServletContainerConfig servletConfig) {

	}

	default void beforeServerStart() {

	}

	default void afterServerStart() {

	}

	default void beforeServerStop() {

	}

	default void afterServerStop() {

	}

}
