/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone;

import info.stasha.testosterone.jersey.JerseyWebRequestTest;
import java.util.LinkedHashSet;
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
import org.glassfish.jersey.test.spi.TestContainerFactory;

/**
 *
 * @author stasha
 */
public class JettyConfiguration implements Configuration {

	public final Set<Throwable> messages = new LinkedHashSet<>();
	public final AtomicReference<Client> client = new AtomicReference<>(null);
	public final String BASE_URI = "http://localhost:9999/";

	private ApplicationHandler application;

	private Server server;

	protected ResourceConfig configuration;
	protected AbstractBinder abstractBinder;

	protected ResourceConfig configure() {
//		enable(TestProperties.LOG_TRAFFIC);

		if (this.configuration == null) {
			this.configuration = new ResourceConfig();
			this.abstractBinder = new AbstractBinder() {
				@Override
				protected void configure() {
				}
			};
		}

		this.configuration.registerInstances(this);
		this.configuration.register(this.abstractBinder);

		init();

		return this.configuration;

	}

	@Override
	public void init() {
		try {
			this.configuration = null;

			server = new Server(9999);
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);

			this.configuration = configure();
			// code for Jersey 2.0
//			this.configuration.register(new ServiceFinderBinder<>(TestContainerFactory.class));
			// code for Jersey 2.1 and higher
			this.configuration.register(new ServiceFinderBinder<>(TestContainerFactory.class, null, RuntimeType.SERVER));

			ServletHolder holder = new ServletHolder();
			holder.setServlet(new ServletContainer(this.configuration));
			holder.setInitOrder(1);
			context.addServlet(holder, "/*");

		} catch (Exception ex) {
			Logger.getLogger(JerseyWebRequestTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void start() throws Exception {
		server.start();

		ResourceConfig config = ResourceConfig.forApplication(this.configuration);

		this.application = new ApplicationHandler(configuration);
		if (application == null) {
			throw new IllegalArgumentException("The application cannot be null");
		}
		
		Client old = client().getAndSet(getClient(application));
		close(old);
	}

	@Override
	public void stop() throws Exception {
		this.server.stop();
	}

	@Override
	public String getBaseUri() {
		return BASE_URI;
	}

	@Override
	public Configuration get() {
		return this;
	}

	@Override
	public Client client() {
		ClientConfig clientConfig = new ClientConfig();
		return ClientBuilder.newClient(clientConfig);
	}


}
