package info.stasha.testosterone.servers;

import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.io.File;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author stasha
 */
public class TomcatServerConfig implements ServerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TomcatServerConfig.class);

    protected Tomcat server;
     StandardContext context = new StandardContext();
    private TestConfig testConfig;
    private ServletContainerConfig servletContainerConfig;

    public TomcatServerConfig() {
        this(null);
    }

    public TomcatServerConfig(TestConfig config) {
        this.testConfig = config;
    }

    @Override
    public ServletContainerConfig getServletContainerConfig() {
        return this.servletContainerConfig;
    }

    @Override
    public void setServletContainerConfig(ServletContainerConfig config) {
        this.servletContainerConfig = config;
    }

    @Override
    public void setConfigurationObject(Object configuration) {

    }

    @Override
    public TestConfig getTestConfig() {
        return this.testConfig;
    }

    @Override
    public void setTestConfig(TestConfig testConfig) {
        this.testConfig = testConfig;
    }

    /**
     * Initializes servlet container by registering context params, listeners,
     * filters and servlets
     */
    protected void initializeServletContainer() {
       
        context.setDocBase(new File(".").getAbsolutePath());
        context.setPath("");
        context.setName("");
        context.addParameter("test", "test");
        context.addLifecycleListener(new Tomcat.FixContextListener());

        // registering context params
        testConfig.getServletContainerConfig().getContextParams().forEach((t, u) -> {
            LOGGER.debug("Setting initial context param {}:{}", t, u);
            context.addParameter(t, u);
        });

        // registering servlet listeners
        testConfig.getServletContainerConfig().getListeners().forEach((t) -> {
            EventListener listener = t.newInstance();
            LOGGER.debug("Adding event listener {} to servlet container.", listener.getClass().getName());
            context.addApplicationEventListener(listener);
            context.addApplicationLifecycleListener(listener);
        });

        // registering servlet filters
        testConfig.getServletContainerConfig().getFilters().forEach((t) -> {
            LOGGER.debug("Adding filter {} to servlet container.", t.toString());

            Filter filter = t.newInstance();
            String filterName = filter.getClass().getName() + String.valueOf(Math.random()).replaceAll("\\.", "");
            FilterDef f = new FilterDef();

            f.setFilter(filter);
            f.setFilterName(filterName);

            t.getInitParams().forEach((k, v) -> {
                f.addInitParameter(k, v);
            });

            FilterMap fm = new FilterMap();
            fm.setFilterName(filterName);

            for (String urlPattern : t.getUrlPattern()) {
                fm.addURLPattern("/*");
            }

            for (Enum dispatcher : t.getDispatchers()) {
                fm.setDispatcher(dispatcher.name());
            }

            context.addFilterDef(f);
            context.addFilterMap(fm);
        });

        Map<String, String> patterns = new HashMap<>();

        // registering servlets
        testConfig.getServletContainerConfig().getServlets().forEach((t) -> {
            LOGGER.debug("Adding servlet {} to servlet container.", t.toString());

            Servlet servlet = t.newInstance();
            String servletName = servlet.getClass().getName() + String.valueOf(Math.random()).replaceAll("\\.", "");
            Wrapper sw = new StandardWrapper();
            sw.setName(servletName);
            sw.setLoadOnStartup(t.getInitOrder());
            sw.setServlet(servlet);
            context.addChild(sw);

            t.getInitParams().forEach((k, v) -> {
                sw.addInitParameter(k, v);
            });

            for (String urlPattern : t.getUrlPattern()) {
                if (!patterns.containsKey(urlPattern)) {
                    patterns.put(urlPattern, servlet.getClass().getName());
                    context.addServletMappingDecoded(urlPattern, servletName);
                } else {
                    throw new RuntimeException("Failed to register servlet with pattern \"" + urlPattern + "\" " + servlet.getClass().getName() + "\n"
                            + "Other servlet " + patterns.get(urlPattern)
                            + "is already registered with this pattern");
                }
            }
        });

        context.setParent(server.getHost());
        server.getHost().addChild(context);
    }

    @Override
    public void start() throws Exception {
        server = new Tomcat();
        server.setPort(this.getTestConfig().getHttpPort());
        initializeServletContainer();

        server.start();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
        server.destroy();
        server = null;
    }

    @Override
    public boolean isRunning() {
        return server != null;
    }

}
