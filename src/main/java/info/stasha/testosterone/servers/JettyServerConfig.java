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
    private TestConfig testConfig;
    private ServletContainerConfig servletContainerConfig;

    public JettyServerConfig() {
        this(null);
    }

    public JettyServerConfig(TestConfig config) {
        this.testConfig = config;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public TestConfig getTestConfig() {
        return this.testConfig;
    }

    /**
     * {@inheritDoc }
     *
     * @param testConfig
     */
    @Override
    public void setTestConfig(TestConfig testConfig) {
        this.testConfig = testConfig;
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
        testConfig.getServletContainerConfig().getContextParams().forEach((t, u) -> {
            LOGGER.debug("Setting initial context param {}:{}", t, u);
            context.setInitParameter(t, u);
        });

        // registering servlet listeners
        testConfig.getServletContainerConfig().getListeners().forEach((t) -> {
            EventListener listener = t.newInstance();
            LOGGER.debug("Adding event listener {} to servlet container.", listener.getClass().getName());
            context.addEventListener(listener);
        });

        // registering servlet filters
        testConfig.getServletContainerConfig().getFilters().forEach((t) -> {
            FilterHolder fh = new FilterHolder();
            Filter filter = t.newInstance();

            LOGGER.debug("Adding filter {} to servlet container.", t.toString());
            fh.setFilter(filter);
            fh.setInitParameters(t.getInitParams());

            for (String urlPattern : t.getUrlPattern()) {
                context.addFilter(fh, urlPattern, t.getDispatchers());
            }
        });

        // registering servlets
        testConfig.getServletContainerConfig().getServlets().forEach((t) -> {
            ServletHolder sh = new ServletHolder();
            Servlet servlet = t.newInstance();

            LOGGER.debug("Adding servlet {} to servlet container.", t.toString());
            sh.setServlet(servlet);
            sh.setInitOrder(t.getInitOrder());

            sh.setInitParameters(t.getInitParams());
            for (String urlPattern : t.getUrlPattern()) {
                context.addServlet(sh, urlPattern);
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
        if (!isRunning()) {
            server = new Server(testConfig.getHttpPort());
            initializeServletContainer();
            server.start();
            String url = "http://localhost:" + testConfig.getHttpPort();
            LOGGER.info("Server started at: " + url);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        if (isRunning() && (testConfig == null || testConfig.isStopServerAfterTestEnds())) {
            server.stop();
        } else if (isRunning() && !testConfig.isStopServerAfterTestEnds()) {
            server.join();
        }
    }

}
