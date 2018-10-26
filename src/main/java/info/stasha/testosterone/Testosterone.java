package info.stasha.testosterone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author stasha
 */
public interface Testosterone {

	static final Logger LOGGER = Logger.getLogger(Testosterone.class.getName());

	//TODO: fix to return thread safe configuration without breaking tests :)
	Map<Class<?>, Configuration> CONFIGURATION = new HashMap<>();

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

	default Configuration getConfiguration() {
		if (!CONFIGURATION.containsKey(this.getClass())) {
			CONFIGURATION.put(this.getClass(), new JettyConfiguration());
		}
		return CONFIGURATION.get(this.getClass());
	}

	default void configure(ResourceConfig config) {

	}

	default void configure(AbstractBinder binder) {

	}

	default void configure(ResourceConfig config, AbstractBinder binder) {

	}

	default void start() throws Exception {
		configure(getConfiguration().getResourceConfig());
		configure(getConfiguration().getAbstractBinder());
		configure(getConfiguration().getResourceConfig(), getConfiguration().getAbstractBinder());
		getConfiguration().init(this);
		getConfiguration().start();
	}

	default void stop() throws Exception {
		getConfiguration().stop();
	}

	@Before
	default void beforeTest() throws Exception {
		start();
	}

	@After
	default void afterTest() throws Exception {
		stop();
	}

}
