package info.stasha.testosterone.jersey;

import info.stasha.testosterone.TestosteroneConfig;
import info.stasha.testosterone.annotation.Configuration.ServerStarts;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Base class for configuring Jersey
 *
 * @author stasha
 */
public class JerseyConfiguration implements TestosteroneConfig {

	protected String baseUri = "http://localhost/";
	protected int port = 9999;
	protected ServerStarts serverStarts = ServerStarts.PARENT_CONFIGURATION;

	protected final Set<Throwable> messages = new LinkedHashSet<>();
	protected final List<Throwable> expectedException = new ArrayList<>();
	protected JerseyConfiguration configuration;
	protected ServletContainerConfig servletContainerConfig = new ServletContainerConfig();

	protected ResourceConfig resourceConfig;
	protected final AtomicReference<Client> client = new AtomicReference<>(null);

	private HttpServer server;
	protected Testosterone resourceObject;
	protected Testosterone testObject;
	protected String testThreadName;

	@Override
	public Testosterone getResourceObject() {
		return resourceObject;
	}

	@Override
	public void setResourceObject(Testosterone resourceObject) {
		this.resourceObject = resourceObject;
	}

	@Override
	public Testosterone getTestObject() {
		return testObject;
	}

	@Override
	public String getTestThreadName() {
		return testThreadName;
	}

	@Override
	public void setTestThreadName(String testThreadName) {
		this.testThreadName = testThreadName;
	}

	@Override
	public void setTestObject(Testosterone testObject) {
		this.testObject = testObject;
	}

	@Override
	public Set<Throwable> getMessages() {
		return messages;
	}

	@Override
	public List<Throwable> getExpectedExceptions() {
		return expectedException;
	}

	@Override
	public void throwErrorMessage() throws Throwable {
		if (getMessages().size() > 0) {
			throw getMessages().iterator().next();
		}
	}

	@Override
	public void throwExpectedException() throws Throwable {
		if (getExpectedExceptions().size() > 0) {
			throw getExpectedExceptions().iterator().next();
		}
	}

	protected ResourceConfig configure() {
		if (this.resourceConfig == null) {
			this.resourceConfig = new ResourceConfig();
		}

		return this.resourceConfig;
	}

	@Override
	public ServletContainerConfig getServletContainerConfig() {
		return null;
	}

	@Override
	public void setServletContainerConfig(ServletContainerConfig servletContainerConfig) {
		this.servletContainerConfig = servletContainerConfig;
	}

	public boolean isRunning() {
		return server != null && server.isStarted();
	}

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

	@Override
	public WebTarget target() {
		try {
			return client().target(getBaseUri());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
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
				client.getAndSet(null);
			} catch (Throwable ex) {
				LOGGER.log(Level.WARNING, "Error closing client instance.", ex);
			}

		}
	}

	@Override
	public ResourceConfig getResourceConfig() {
		return configure();
	}

	@Override
	public void setResourceConfig(ResourceConfig resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	protected void createServer() throws URISyntaxException {
		server = GrizzlyHttpServerFactory.createHttpServer(new URI(getBaseUri()), configure());
	}

	protected void prepare() {
		Client old = client.getAndSet(getClient());
		closeClient(old);
	}

	protected void cleanUp() {
		this.resourceConfig = null;

		closeClient(client.get());
	}

	@Override
	public void start() throws Exception {
		prepare();

		if (server != null && !server.isStarted()) {
			server.start();
		}
	}

	@Override
	public void stop() throws Exception {
		if (server != null && server.isStarted()) {
			server.stop();
		}
		cleanUp();
	}

	@Override
	public void initConfiguration(Testosterone obj) {
		this.resourceObject = obj;
		this.resourceConfig.register(obj.getClass());

		init();
	}

	@Override
	public void init() {
		try {
			createServer();

		} catch (Exception ex) {
			Logger.getLogger(JettyConfiguration.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void setServerStarts(ServerStarts serverStarts) {
		this.serverStarts = serverStarts;
	}

	@Override
	public String getBaseUri() {
		return UriBuilder.fromUri(this.baseUri).port(getPort()).build().toString();
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public ServerStarts getServerStarts() {
		return this.serverStarts;
	}

	@Override
	public String toString() {
		return "JerseyConfiguration{" + "testObject=" + testObject.getClass().getName() + '}';
	}

}
