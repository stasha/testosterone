package info.stasha.testosterone.servlet;

import info.stasha.testosterone.TestConfig;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.DispatcherType;

/**
 * "DTO" container used for configuring real servlet container/s.
 *
 * @author stasha
 */
public class ServletContainerConfig {

    private String jerseyServletPath = "/*";
    private final Set<Servlet> servlets = new LinkedHashSet<>();
    private final Set<Filter> filters = new LinkedHashSet<>();
    private final Set<Listener> listeners = new LinkedHashSet<>();
    private final Map<String, String> contextParams = new LinkedHashMap<>();

    private final TestConfig config;

    public ServletContainerConfig(TestConfig config) {
        this.config = config;
    }

    /**
     * Sets Jersey servlet path.<br>
     * Note that by default Jersey servlet path is "/*". So if you want to
     * register other servlet under same path "/*", exception will be thrown.
     *
     * @param path
     * @return
     */
    public ServletContainerConfig setJerseyServletPath(String path) {
        this.jerseyServletPath = path;
        return this;
    }

    /**
     * Returns Jersey servlet path.
     *
     * @return
     */
    public String getJerseyServletPath() {
        return jerseyServletPath;
    }

    /**
     * Adds javax.servlet.Servlet class and url pattern.
     *
     * @param clazz
     * @param urlPattern
     * @return
     */
    public Servlet addServlet(Class<? extends javax.servlet.Servlet> clazz, String urlPattern) {
        Servlet s = new Servlet(clazz, urlPattern);
        servlets.add(s);
        return s;
    }

    /**
     * Adds Servlet instance.
     *
     * @param servlet
     * @return
     */
    public Servlet addServlet(Servlet servlet) {
        servlets.add(servlet);
        return servlet;
    }

    /**
     * Adds javax.servlet.Servlet class and url patterns.
     *
     * @param clazz
     * @param urlPattern
     * @return
     */
    public Servlet addServlet(Class<? extends javax.servlet.Servlet> clazz, String[] urlPattern) {
        Servlet s = new Servlet(clazz, urlPattern);
        servlets.add(s);
        return s;
    }

    /**
     * Adds javax.servlet.Servlet instance and url pattern.
     *
     * @param servlet
     * @param urlPattern
     * @return
     */
    public Servlet addServlet(javax.servlet.Servlet servlet, String urlPattern) {
        Servlet s = new Servlet(servlet, urlPattern);
        servlets.add(s);
        return s;
    }

    /**
     * Adds javax.servlet.Servlet instance and url patterns.
     *
     * @param servlet
     * @param urlPattern
     * @return
     */
    public Servlet addServlet(javax.servlet.Servlet servlet, String[] urlPattern) {
        Servlet s = new Servlet(servlet, urlPattern);
        servlets.add(s);
        return s;
    }

    /**
     * Adds javax.servlet.Filter class and url pattern.
     *
     * @param clazz
     * @param urlPattern
     * @return
     */
    public Filter addFilter(Class<? extends javax.servlet.Filter> clazz, String urlPattern) {
        Filter f = new Filter(clazz, urlPattern);
        filters.add(f);
        return f;
    }

    /**
     * Adds javax.servlet.Filter class and url patterns.
     *
     * @param clazz
     * @param urlPattern
     * @return
     */
    public Filter addFilter(Class<? extends javax.servlet.Filter> clazz, String[] urlPattern) {
        Filter f = new Filter(clazz, urlPattern);
        filters.add(f);
        return f;
    }

    /**
     * Adds javax.servlet.Filter class and url patterns.
     *
     * @param clazz
     * @param urlPattern
     * @param dispatchers
     * @return
     */
    public Filter addFilter(Class<? extends javax.servlet.Filter> clazz, String[] urlPattern, EnumSet<DispatcherType> dispatchers) {
        Filter f = new Filter(clazz, urlPattern);
        f.setDispatchers(dispatchers);
        filters.add(f);
        return f;
    }

    /**
     * Adds javax.servlet.Filter instance and url pattern.
     *
     * @param filter
     * @param urlPattern
     * @return
     */
    public Filter addFilter(javax.servlet.Filter filter, String urlPattern) {
        Filter f = new Filter(filter, urlPattern);
        filters.add(f);
        return f;
    }

    /**
     * Adds javax.servlet.Filter instance and url patterns.
     *
     * @param filter
     * @param urlPattern
     * @return
     */
    public Filter addFilter(javax.servlet.Filter filter, String[] urlPattern) {
        Filter f = new Filter(filter, urlPattern);
        filters.add(f);
        return f;
    }

    /**
     * Adds javax.servlet.Filter instance, url patterns and dispatcher types.
     *
     * @param filter
     * @param urlPattern
     * @param dispatchers
     * @return
     */
    public Filter addFilter(javax.servlet.Filter filter, String[] urlPattern, EnumSet<DispatcherType> dispatchers) {
        Filter f = new Filter(filter, urlPattern);
        f.setDispatchers(dispatchers);
        filters.add(f);
        return f;
    }

    /**
     * AddsFilter instance.
     *
     * @param filter
     * @return
     */
    public Filter addFilter(Filter filter) {
        filters.add(filter);
        return filter;
    }

    /**
     * Adds servlet EventListener class.
     *
     * @param clazz
     * @return
     */
    public ServletContainerConfig addListener(Class<? extends EventListener> clazz) {
        listeners.add(new Listener(clazz));
        return this;
    }

    /**
     * Adds servlet EventListener instance.
     *
     * @param listener
     * @return
     */
    public ServletContainerConfig addListener(EventListener listener) {
        listeners.add(new Listener(listener));
        return this;
    }

    /**
     * Adds Listener instance.
     *
     * @param listener
     * @return
     */
    public ServletContainerConfig addListener(Listener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Add servlet context param.
     *
     * @param name
     * @param value
     * @return
     */
    public ServletContainerConfig addContextParam(String name, String value) {
        contextParams.put(name, value);
        return this;
    }

    /**
     * Returns all registered Servlets.
     *
     * @return
     */
    public Set<Servlet> getServlets() {
        return servlets;
    }

    /**
     * Returns all registered Filters.
     *
     * @return
     */
    public Set<Filter> getFilters() {
        return filters;
    }

    /**
     * Returns all registered Listeners.
     *
     * @return
     */
    public Set<? extends Listener> getListeners() {
        return listeners;
    }

    /**
     * Returns all registered context params.
     *
     * @return
     */
    public Map<String, String> getContextParams() {
        return contextParams;
    }

}
