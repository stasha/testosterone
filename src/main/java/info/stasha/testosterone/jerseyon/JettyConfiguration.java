package info.stasha.testosterone.jerseyon;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Configuration for running Jersey app with Jetty.
 *
 * @author stasha
 */
public class JettyConfiguration extends JerseyConfiguration {

	protected Server server;

	@Override
	protected void createServer() {
		server = new Server(9999);
		registerServlets();
	}

	protected void registerServlets() {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		ServletHolder holder = new ServletHolder();
		holder.setServlet(new ServletContainer(this.resourceConfig));
		holder.setInitOrder(1);
		context.addServlet(holder, "/*");
	}

	@Override
	public void start() throws Exception {
		if (server != null && !server.isRunning()) {
			server.start();
		}
		prepare();
	}

	@Override
	public void stop() throws Exception {
		cleanUp();
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}

}
