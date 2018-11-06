package info.stasha.testosterone.jersey;

import info.stasha.testosterone.annotation.Configuration.ServerStarts;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import info.stasha.testosterone.ServerConfig;

/**
 * Base class for configuring Jersey
 *
 * @author stasha
 */
public class GrizzlyServerConfig implements ServerConfig {

	protected String baseUri = "http://localhost/";
	protected int port = 9999;
	protected ServerStarts serverStarts = ServerStarts.PARENT_CONFIGURATION;

	protected final Set<Throwable> messages = new LinkedHashSet<>();
	protected final List<Throwable> expectedException = new ArrayList<>();
	protected ServletContainerConfig servletContainerConfig = new ServletContainerConfig();

	protected ResourceConfig resourceConfig;
	protected final AtomicReference<Client> client = new AtomicReference<>(null);

	private HttpServer server;
	protected Testosterone resourceObject;
	protected Testosterone testObject;
	protected String testThreadName;

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public Testosterone getResourceObject() {
		return resourceObject;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param resourceObject
	 */
	@Override
	public void setResourceObject(Testosterone resourceObject) {
		this.resourceObject = resourceObject;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public Testosterone getTestObject() {
		return testObject;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public String getTestThreadName() {
		return testThreadName;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param testThreadName
	 */
	@Override
	public void setTestThreadName(String testThreadName) {
		this.testThreadName = testThreadName;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param testObject
	 */
	@Override
	public void setTestObject(Testosterone testObject) {
		this.testObject = testObject;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public Set<Throwable> getMessages() {
		return messages;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public List<Throwable> getExpectedExceptions() {
		return expectedException;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws Throwable
	 */
	@Override
	public void throwErrorMessage() throws Throwable {
		if (getMessages().size() > 0) {
			throw getMessages().iterator().next();
		}
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws Throwable
	 */
	@Override
	public void throwExpectedException() throws Throwable {
		if (getExpectedExceptions().size() > 0) {
			throw getExpectedExceptions().iterator().next();
		}
	}

	/**
	 * Returns ResourceConfig.
	 *
	 * @return
	 */
	protected ResourceConfig configure() {
		if (this.resourceConfig == null) {
			this.resourceConfig = new ResourceConfig();
		}

		return this.resourceConfig;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public ServletContainerConfig getServletContainerConfig() {
		return null;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param servletContainerConfig
	 */
	@Override
	public void setServletContainerConfig(ServletContainerConfig servletContainerConfig) {
		this.servletContainerConfig = servletContainerConfig;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public boolean isRunning() {
		return server != null && server.isStarted();
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public Client client() {
		if (client.get() == null) {
			client.getAndSet(getClient());
		}
		return client.get();
	}

	protected Client getClient() {
		ClientConfig clientConfig = new ClientConfig(this.resourceConfig);
		return ClientBuilder.newClient(clientConfig);
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public WebTarget target() {
		try {
			return client().target(getBaseUri());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Closes client.
	 *
	 * @param clients
	 */
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
				client.getAndSet(null);
			} catch (Throwable ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}

		}
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public ResourceConfig getResourceConfig() {
		return configure();
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param resourceConfig
	 */
	@Override
	public void setResourceConfig(ResourceConfig resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	/**
	 * Creates server.
	 *
	 * @throws URISyntaxException
	 */
	protected void createServer() throws URISyntaxException {
		server = GrizzlyHttpServerFactory.createHttpServer(new URI(getBaseUri()), configure());
	}

	/**
	 * Prepares whatever :).
	 */
	protected void prepare() {
		Client old = client.getAndSet(getClient());
		closeClient(old);
	}

	/**
	 * Cleans up configuration.
	 */
	protected void cleanUp() {
		this.resourceConfig = null;

		closeClient(client.get());
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws Exception
	 */
	@Override
	public void start() throws Exception {
		prepare();

		if (server != null && !server.isStarted()) {
			server.start();
		}
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws Exception
	 */
	@Override
	public void stop() throws Exception {
		if (server != null && server.isStarted()) {
			server.stop();
		}
		cleanUp();
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param obj
	 */
	@Override
	public void initConfiguration(Testosterone obj) {
		this.resourceObject = obj;
		this.resourceConfig.register(obj.getClass());

		init();
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public void init() {
		try {
			createServer();

		} catch (Exception ex) {
			Logger.getLogger(JettyServerConfig.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param baseUri
	 */
	@Override
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param port
	 */
	@Override
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param serverStarts
	 */
	@Override
	public void setServerStarts(ServerStarts serverStarts) {
		this.serverStarts = serverStarts;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public String getBaseUri() {
		return UriBuilder.fromUri(this.baseUri).port(getPort()).build().toString();
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public int getPort() {
		return this.port;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public ServerStarts getServerStarts() {
		return this.serverStarts;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "JerseyConfiguration{" + "testObject=" + testObject.getClass().getName() + '}';
	}

}
