package info.stasha.testosterone.servers;

import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.util.EventListener;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration for running Jersey app within Jetty server.
 *
 * @author stasha
 */
public class JettyServerConfig implements ServerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JettyServerConfig.class);

    protected Server server;
    private final TestConfig config;
    private ServletContainerConfig servletContainerConfig;

    public JettyServerConfig(TestConfig config) {
        this.config = config;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public TestConfig getTestConfig() {
        return this.config;
    }

    /**
     * {@inheritDoc }
     *
     * @param configuration
     */
    @Override
    public void setConfigurationObject(Object configuration) {

    }

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
     * @param config
     */
    @Override
    public void setServletContainerConfig(ServletContainerConfig config) {
        this.servletContainerConfig = config;
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
     * Initializes servlet container by registering context params, listeners,
     * filters and servlets
     */
    protected void initializeServletContainer() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SERVLET_MAJOR_VERSION);
        context.setContextPath("/");
        server.setHandler(context);

        // registering context params
        config.getServletContainerConfig().getContextParams().forEach((t, u) -> {
            LOGGER.debug("Setting initial context param {}:{}", t, u);
            context.setInitParameter(t, u);
        });

        // registering servlet listeners
        config.getServletContainerConfig().getListeners().forEach((t) -> {
            EventListener listener;
            if (t.getListener() == null) {
                try {
                    listener = t.getClazz().newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    LOGGER.error("Failed to create new listener.", ex);
                    throw new RuntimeException(ex);
                }
            } else {
                listener = t.getListener();
            }

            LOGGER.debug("Adding event listener {} to servlet container.", listener.getClass().getName());
            context.addEventListener(listener);
        });

        // registering servlet filters
        config.getServletContainerConfig().getFilters().forEach((t) -> {
            try {
                FilterHolder fh = new FilterHolder();
                Filter filter;
                if (t.getFilter() == null) {
                    filter = (Filter) t.getClazz().newInstance();
                } else {
                    filter = t.getFilter();
                }

                LOGGER.debug("Adding filter {} to servlet container.", t.toString());
                fh.setFilter(filter);
                fh.setInitParameters(t.getInitParams());

                for (String urlPattern : t.getUrlPattern()) {
                    context.addFilter(fh, urlPattern, t.getDispatchers());
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.error("Failed to create new filter.", ex);
                throw new RuntimeException(ex);
            }
        });

        // registering servlets
        config.getServletContainerConfig().getServlets().forEach((t) -> {
            try {
                ServletHolder sh = new ServletHolder();
                Servlet servlet;
                if (t.getServlet() == null) {
                    servlet = (Servlet) t.getClazz().newInstance();
                } else {
                    servlet = t.getServlet();
                }

                LOGGER.debug("Adding servlet {} to servlet container.", t.toString());
                sh.setServlet(servlet);
                sh.setInitOrder(t.getInitOrder());

                sh.setInitParameters(t.getInitParams());
                for (String urlPattern : t.getUrlPattern()) {
                    context.addServlet(sh, urlPattern);
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.error("Failed to create new servlet.", ex);
                throw new RuntimeException(ex);
            }
        });

    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        server = new Server(config.getHttpPort());
        initializeServletContainer();
        if (!isRunning()) {
            try {
//                LOGGER.info("Starting server at: {} for test {}", getBaseUri(), this.getMainThreadTestObject().getClass().getName());
                server.start();
            } catch (java.net.BindException ex) {
                LOGGER.error("Server failed to start.", ex);
                throw ex;
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
        if (isRunning()) {
//            LOGGER.info("Stopping server at: {} for test {}", getBaseUri(), this.getMainThreadTestObject().getClass().getName());
            try {
                server.stop();
            } catch (Exception ex) {
                LOGGER.error("Server failed to stop.", ex);
                throw ex;
            }
        }
    }

}
