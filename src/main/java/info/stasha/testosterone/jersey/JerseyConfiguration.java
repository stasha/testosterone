package info.stasha.testosterone.jersey;

import static info.stasha.testosterone.jersey.Testosterone.LOGGER;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Base class for configuring Jersey
 *
 * @author stasha
 */
public class JerseyConfiguration {

	protected static String BASE_URI = "http://localhost:9999/";

	protected final Set<Throwable> messages = new LinkedHashSet<>();
	protected final List<Throwable> expectedException = new ArrayList<>();
	protected JerseyConfiguration configuration;
	protected ResourceConfig resourceConfig;
	protected AbstractBinder abstractBinder;
	protected final AtomicReference<Client> client = new AtomicReference<>(null);

	private HttpServer server;
	protected Object testObj;

	public Set<Throwable> getMessages() {
		return messages;
	}

	public List<Throwable> getExpectedExceptions() {
		return expectedException;
	}

	protected ResourceConfig configure() {
		if (this.resourceConfig == null) {
			this.resourceConfig = new ResourceConfig();
		}

		createAbstractBinder();
		return this.resourceConfig;
	}

	protected void createAbstractBinder() {
		if (this.abstractBinder == null) {
			this.abstractBinder = new AbstractBinder() {
				@Override
				protected void configure() {
				}
			};
		}
	}

	public ServletContainerConfig getServletContainerConfig() {
		throw new NotSupportedException("servlet configuration is not supported by default jersey config.");
	}

	protected boolean isRunning() {
		return server != null && server.isStarted();
	}

	public String getBaseUri() {
		return BASE_URI;
	}

	public void setBaseUri(String baseUri) {
		BASE_URI = baseUri;
	}

	public Client client() {
		if(client.get() == null) {
			client.getAndSet(getClient());
		}
		return client.get();
	}

	protected Client getClient() {
		ClientConfig clientConfig = new ClientConfig(this.resourceConfig);
		return ClientBuilder.newClient(clientConfig);
	}

	public WebTarget target() {
		return client().target(BASE_URI);
	}

	protected void closeClient(final Client... clients) {
		if (clients == null || clients.length == 0) {
			return;
		}

		for (Client c : clients) {
			if (c == null) {
				continue;
			}
			try {
				c.close();
			} catch (Throwable ex) {
				LOGGER.log(Level.WARNING, "Error closing client instance.", ex);
			}

		}
	}

	public ResourceConfig getResourceConfig() {
		return configure();
	}

	public AbstractBinder getAbstractBinder() {
		return this.abstractBinder;
	}

	protected void createServer() throws URISyntaxException {
		server = GrizzlyHttpServerFactory.createHttpServer(new URI(BASE_URI), configure());
	}

	protected void prepare() {
		Client old = client.getAndSet(getClient());
		closeClient(old);
	}

	protected void cleanUp() {
		this.resourceConfig = null;
		this.abstractBinder = null;

		closeClient(client.get());
	}

	public void start() throws Exception {
		prepare();

		if (server != null && !server.isStarted()) {
			server.start();
		}
	}

	public void stop() throws Exception {
		if (server != null && server.isStarted()) {
			server.stop();
		}
		cleanUp();
	}

	public void init(Object obj) throws Exception {
		this.testObj = obj;

		try {

			this.resourceConfig.register(this.testObj.getClass());
			this.resourceConfig.register(this.abstractBinder);

			createServer();

		} catch (Exception ex) {
			Logger.getLogger(JettyConfiguration.class.getName()).log(Level.SEVERE, null, ex);
			throw ex;
		}
	}

}
