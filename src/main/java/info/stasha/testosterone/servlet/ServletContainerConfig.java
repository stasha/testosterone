package info.stasha.testosterone.servlet;

import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * "DTO" container used for configuring real servlet container/s.
 *
 * @author stasha
 */
public class ServletContainerConfig {

	private String jerseyServletPath = "/*";
	private final Set<Servlet> servlets = new LinkedHashSet<>();
	private final Set<Filter> filters = new LinkedHashSet<>();
	private final Set<EventListener> listeners = new LinkedHashSet<>();
	private final Map<String, String> contextParams = new LinkedHashMap<>();

	public ServletContainerConfig setJerseyServletPath(String path) {
		this.jerseyServletPath = path;
		return this;
	}

	public String getJerseyServletPath() {
		return jerseyServletPath;
	}

	public Servlet addServlet(Class<? extends javax.servlet.Servlet> clazz, String urlPattern) {
		Servlet s = new Servlet(clazz, urlPattern);
		servlets.add(s);
		return s;
	}

	public Servlet addServlet(Class<? extends javax.servlet.Servlet> clazz, String[] urlPattern) {
		Servlet s = new Servlet(clazz, urlPattern);
		servlets.add(s);
		return s;
	}

	public Servlet addServlet(javax.servlet.Servlet servlet, String urlPattern) {
		Servlet s = new Servlet(servlet, urlPattern);
		servlets.add(s);
		return s;
	}

	public Servlet addServlet(javax.servlet.Servlet servlet, String[] urlPattern) {
		Servlet s = new Servlet(servlet, urlPattern);
		servlets.add(s);
		return s;
	}

	public Filter addFilter(Class<? extends javax.servlet.Filter> clazz, String urlPattern) {
		Filter f = new Filter(clazz, urlPattern);
		filters.add(f);
		return f;
	}

	public Filter addFilter(Class<? extends javax.servlet.Filter> clazz, String[] urlPattern) {
		Filter f = new Filter(clazz, urlPattern);
		filters.add(f);
		return f;
	}

	public Filter addFilter(javax.servlet.Filter filter, String urlPattern) {
		Filter f = new Filter(filter, urlPattern);
		filters.add(f);
		return f;
	}

	public Filter addFilter(javax.servlet.Filter filter, String[] urlPattern) {
		Filter f = new Filter(filter, urlPattern);
		filters.add(f);
		return f;
	}

	public ServletContainerConfig addListener(Class<? extends EventListener> clazz) {
		try {
			listeners.add(clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(ServletContainerConfig.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
		return this;
	}

	public ServletContainerConfig addListener(EventListener listener) {
		listeners.add(listener);
		return this;
	}

	public ServletContainerConfig addContextParam(String name, String value) {
		contextParams.put(name, value);
		return this;
	}

	public Set<Servlet> getServlets() {
		return servlets;
	}

	public Set<Filter> getFilters() {
		return filters;
	}

	public Set<? extends EventListener> getListeners() {
		return listeners;
	}

	public Map<String, String> getContextParams() {
		return contextParams;
	}

}
