package info.stasha.testosterone.jersey;

import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Configuration for running Jersey app within Jetty server.
 *
 * @author stasha
 */
public class JettyServerConfig extends GrizzlyServerConfig {

	protected final ServletContainerConfig servletContainerConfig = new ServletContainerConfig();
	protected Server server;

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public ServletContainerConfig getServletContainerConfig() {
		return this.servletContainerConfig;
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
	 */
	@Override
	protected void createServer() {
		server = new Server(getPort());
		initializeServletContainer();
	}

	/**
	 * Initializes servlet container by registering context params, listeners,
	 * filters and servlets
	 */
	protected void initializeServletContainer() {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SERVLET_MAJOR_VERSION);
		context.setContextPath("/");
		server.setHandler(context);

		// registering context params
		servletContainerConfig.getContextParams().forEach((t, u) -> {
			context.setInitParameter(t, u);
		});

		// registering servlet listeners
		servletContainerConfig.getListeners().forEach((t) -> {
			if (t.getListener() == null) {
				try {
					context.addEventListener(t.getClazz().newInstance());
				} catch (InstantiationException | IllegalAccessException ex) {
					Logger.getLogger(JettyServerConfig.class.getName()).log(Level.SEVERE, null, ex);
				}
			} else {
				context.addEventListener(t.getListener());
			}
		});

		// registering servlet filters
		servletContainerConfig.getFilters().forEach((t) -> {
			try {
				FilterHolder fh = new FilterHolder();
				if (t.getFilter() == null) {
					fh.setFilter((Filter) t.getClazz().newInstance());
				} else {
					fh.setFilter(t.getFilter());
				}
				fh.setInitParameters(t.getInitParams());

				for (String urlPattern : t.getUrlPattern()) {
					context.addFilter(fh, urlPattern, t.getDispatchers());
				}
			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(JettyServerConfig.class.getName()).log(Level.SEVERE, null, ex);
				throw new RuntimeException(ex);
			}
		});

		// registering servlets
		servletContainerConfig.getServlets().forEach((t) -> {
			try {
				ServletHolder sh = new ServletHolder();
				if (t.getServlet() == null) {
					sh.setServlet((Servlet) t.getClazz().newInstance());
				} else {
					sh.setServlet(t.getServlet());
				}
				sh.setInitOrder(t.getInitOrder());

				sh.setInitParameters(t.getInitParams());
				for (String urlPattern : t.getUrlPattern()) {
					context.addServlet(sh, urlPattern);
				}
			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(JettyServerConfig.class.getName()).log(Level.SEVERE, null, ex);
				throw new RuntimeException(ex);
			}
		});

		// registering Jersey servlet
		ServletHolder holder = new ServletHolder();
		holder.setServlet(new ServletContainer(this.resourceConfig));
		holder.setInitOrder(1);
		context.addServlet(holder, servletContainerConfig.getJerseyServletPath());

	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws Exception
	 */
	@Override
	public void start() throws Exception {
		prepare();

		if (server != null && !server.isRunning()) {
			try {
				System.out.println("Starting server: " + getBaseUri() + " for test: " + this.getResourceObject().getClass().getName());
				server.start();
			} catch (java.net.BindException ex) {
				throw new RuntimeException("Server failed to start for resource: " + getResourceObject(), ex);
			}
		}
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws Exception
	 */
	@Override
	public void stop() throws Exception {
		cleanUp();
		if (server != null && server.isRunning()) {
			System.out.println("");
			System.out.println("Stopping server " + getBaseUri() + " for test: " + this.getResourceObject().getClass().getName());
			server.stop();
		}
	}

}
