package info.stasha.testosterone;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author stasha
 */
public interface Testosterone {

	static final Logger LOGGER = Logger.getLogger(Testosterone.class.getName());

	//TODO fix this
	Configuration configuration = Configuration.newInstance();

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
		return configuration;
	}

	default void configure(ResourceConfig config) {

	}

	default void setConfiguration(Configuration conf) {
		getConfiguration().set(configuration);
	}

	@Before
	default void setUp() throws Exception {
		configure(getConfiguration().getResourceConfig());
		getConfiguration().init(this);
		getConfiguration().start();
	}

	@After
	default void tearDown() throws Exception {
		configuration.stop();
	}

}
