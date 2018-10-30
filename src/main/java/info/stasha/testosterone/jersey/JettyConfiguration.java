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
public class JettyConfiguration extends JerseyConfiguration {

	protected final ServletContainerConfig servletContainerConfig = new ServletContainerConfig();
	protected Server server;

	@Override
	public ServletContainerConfig getServletContainerConfig() {
		return this.servletContainerConfig;
	}

	@Override
	protected boolean isRunning() {
		return server != null && server.isStarted();
	}

	@Override
	protected void createServer() {
		server = new Server(9999);
		registerServlets();
	}

	protected void registerServlets() {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SERVLET_MAJOR_VERSION);
		context.setContextPath("/");
		server.setHandler(context);

		servletContainerConfig.getContextParams().forEach((t, u) -> {
			context.setInitParameter(t, u);
		});

		servletContainerConfig.getListeners().forEach((t) -> {
			context.addEventListener(t);
		});

		servletContainerConfig.getServlets().forEach((t) -> {
			try {
				ServletHolder sh = new ServletHolder();
				if (t.getServlet() == null) {
					sh.setServlet((Servlet) t.getClazz().newInstance());
				} else {
					sh.setServlet(t.getServlet());
				}
				sh.setInitOrder(t.getInitOrder());
//				
				sh.setInitParameters(t.getInitParams());
				context.addServlet(sh, t.getUrlPattern()[0]);
			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(JettyConfiguration.class.getName()).log(Level.SEVERE, null, ex);
				throw new RuntimeException(ex);
			}
		});

		servletContainerConfig.getFilters().forEach((t) -> {
			try {
				FilterHolder fh = new FilterHolder();
				if (t.getFilter() == null) {
					fh.setFilter((Filter) t.getClazz().newInstance());
				} else {
					fh.setFilter(t.getFilter());
				}
				fh.setInitParameters(t.getInitParams());

				context.addFilter(fh, t.getUrlPattern()[0], t.getDispatchers());
			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(JettyConfiguration.class.getName()).log(Level.SEVERE, null, ex);
			}
		});

		ServletHolder holder = new ServletHolder();
		holder.setServlet(new ServletContainer(this.resourceConfig));
		holder.setInitOrder(1);
		context.addServlet(holder, servletContainerConfig.getJerseyServletPath());

	}

	@Override
	public void start() throws Exception {
		prepare();

		if (server != null && !server.isRunning()) {
			server.start();
		}
	}

	@Override
	public void stop() throws Exception {
		cleanUp();
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}

}
