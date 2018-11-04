package info.stasha.testosterone.jersey;

import info.stasha.testosterone.annotation.Configuration;
import static info.stasha.testosterone.jersey.Testosterone.LOGGER;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.lang.annotation.Annotation;
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
public class JerseyConfiguration implements Configuration {

	protected boolean managedByParentConfiguration = false;
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

	public boolean isManagedByParentConfiguration() {
		return managedByParentConfiguration;
	}

	public void setManagedByParentConfiguration(boolean managedByParentConfiguration) {
		this.managedByParentConfiguration = managedByParentConfiguration;
	}

	public Testosterone getResourceObject() {
		return resourceObject;
	}

	public void setResourceObject(Testosterone resourceObject) {
		this.resourceObject = resourceObject;
	}

	public Testosterone getTestObject() {
		return testObject;
	}

	public String getTestThreadName() {
		return testThreadName;
	}

	public void setTestThreadName(String testThreadName) {
		this.testThreadName = testThreadName;
	}

	public void setTestObject(Testosterone testObject) {
		this.testObject = testObject;
	}

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

		return this.resourceConfig;
	}

	public ServletContainerConfig getServletContainerConfig() {
		return null;
	}

	public void setServletContainerConfig(ServletContainerConfig servletContainerConfig) {
		this.servletContainerConfig = servletContainerConfig;
	}

	protected boolean isRunning() {
		return server != null && server.isStarted();
	}

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

	public WebTarget target() {
		try {
		return client().target(baseUri());
		}catch(Exception ex){
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

	public ResourceConfig getResourceConfig() {
		return configure();
	}

	public void setResourceConfig(ResourceConfig resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	protected void createServer() throws URISyntaxException {
		server = GrizzlyHttpServerFactory.createHttpServer(new URI(baseUri()), configure());
	}

	protected void prepare() {
		Client old = client.getAndSet(getClient());
		closeClient(old);
	}

	protected void cleanUp() {
		this.resourceConfig = null;

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

	public void initConfiguration(Testosterone obj) {
		this.resourceObject = obj;
		this.resourceConfig.register(obj.getClass());
	}

	public void init() throws Exception {
		try {
			createServer();

		} catch (Exception ex) {
			Logger.getLogger(JettyConfiguration.class.getName()).log(Level.SEVERE, null, ex);
			throw ex;
		}
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setServerStarts(ServerStarts serverStarts) {
		this.serverStarts = serverStarts;
	}

	@Override
	public String baseUri() {
		return UriBuilder.fromUri(this.baseUri).port(port()).build().toString();
	}

	@Override
	public int port() {
		return this.port;
	}

	@Override
	public ServerStarts serverStarts() {
		return this.serverStarts;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Configuration.class;
	}

	@Override
	public Class<?> configuration() {
		return this.getClass();
	}

	@Override
	public String toString() {
		return "JerseyConfiguration{" + "testObject=" + testObject.getClass().getName() + '}';
	}
	
	

}
