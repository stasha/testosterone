package info.stasha.testosterone;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ApplicationHandler;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author stasha
 */
public interface Testosterone {

	public static final Logger LOGGER = Logger.getLogger(Testosterone.class.getName());

	public final Set<Throwable> messages = new LinkedHashSet<>();

	public final String BASE_URL = "http://localhost:9999/";

	public final Configuration configuration = new JettyConfiguration();

	public default Set<Throwable> getMessages() {
		return messages;
	}

	Throwable getThrownException();

	void setThrownException(Throwable throwable);

	public default Client getClient(ApplicationHandler applicationHandler) {
		ClientConfig clientConfig = new ClientConfig();
		return ClientBuilder.newClient(clientConfig);
	}

	public default WebTarget target() {
		return client().target(BASE_URL);
	}

	public default WebTarget target(String path) {
		return target().path(path);
	}

	public default Client client() {
		return getConfiguration().client();
	}

	public default Configuration getConfiguration() {
		return configuration.get();
	}

	public default void init() {
		getConfiguration().init();
	}

	@Before
	public default void setUp() throws Exception {
		getConfiguration().start();

	}

	@After
	public void tearDown() throws Exception {
		try {
			server.stop();
		} finally {
			Client old = client.getAndSet(null);
			close(old);
		}
	}

	public default void close(final Client... clients) {
		if (clients == null || clients.length == 0) {
			return;
		}

		for (Client c : clients) {
			if (c == null) {
				continue;
			}
			try {
				c.close();
			} catch (Throwable t) {
				LOGGER.log(Level.WARNING, "Error closing a client instance.", t);
			}

		}
	}

}
