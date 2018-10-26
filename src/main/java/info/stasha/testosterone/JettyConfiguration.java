package info.stasha.testosterone;

import static info.stasha.testosterone.Testosterone.LOGGER;
import info.stasha.testosterone.jersey.JerseyWebRequestTest;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.internal.ServiceFinderBinder;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author stasha
 */
public class JettyConfiguration implements Configuration {

	private Object testObj;

	public final AtomicReference<Client> client = new AtomicReference<>(null);
	public final String BASE_URI = "http://localhost:9999/";

	protected final Set<Throwable> messages = new LinkedHashSet<>();
	protected final List<Throwable> expectedException = new ArrayList<>();

	protected Configuration configuration;
	private Server server;
	protected ResourceConfig resourceConfig;
	protected AbstractBinder abstractBinder;

	public Set<Throwable> getMessages() {
		return messages;
	}

	public List<Throwable> getExpectedExceptions() {
		return expectedException;
	}

	protected ResourceConfig configure() {
//		enable(TestProperties.LOG_TRAFFIC);

		if (this.resourceConfig == null) {
			this.resourceConfig = new ResourceConfig();
			this.abstractBinder = new AbstractBinder() {
				@Override
				protected void configure() {
				}
			};
		}

		
		this.resourceConfig.register(this.abstractBinder);

		return this.resourceConfig;

	}

	@Override
	public void init(Object obj) {
		this.testObj = obj;
		try {
			
			this.resourceConfig.registerInstances(this.testObj);

			server = new Server(9999);
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);

			// code for Jersey 2.0
//			this.resourceConfig.register(new ServiceFinderBinder<>(TestContainerFactory.class));
			// code for Jersey 2.1 and higher
			this.resourceConfig.register(new ServiceFinderBinder<>(Testosterone.class, null, RuntimeType.SERVER));

			ServletHolder holder = new ServletHolder();
			holder.setServlet(new ServletContainer(this.resourceConfig));
			holder.setInitOrder(1);
			context.addServlet(holder, "/*");

		} catch (Exception ex) {
			Logger.getLogger(JerseyWebRequestTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void start() throws Exception {
		server.start();
		Client old = client.getAndSet(getClient());
		close(old);
	}

	@Override
	public void stop() throws Exception {
		this.configuration = null;
		this.server.stop();
	}

	@Override
	public String getBaseUri() {
		return BASE_URI;
	}

	@Override
	public void set(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration get() {
		if (this.configuration == null) {
			this.configuration = this;
		}
		return configuration;
	}

	@Override
	public Client client() {
		return client.get();
	}

	protected Client getClient() {
		ClientConfig clientConfig = new ClientConfig(this.resourceConfig);
		return ClientBuilder.newClient(clientConfig);
	}

	@Override
	public WebTarget target() {
		return client().target(BASE_URI);
	}

	protected void close(final Client... clients) {
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
				LOGGER.log(Level.WARNING, "Error closing client instance.", t);
			}

		}
	}

	@Override
	public ResourceConfig getResourceConfig() {
		return configure();
	}

}
