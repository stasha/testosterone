package info.stasha.testosterone.servlet;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Testosterone representation of javax.servlet.Servlet
 *
 * @author stasha
 */
public class Servlet {

	private Class<? extends javax.servlet.Servlet> clazz;
	private javax.servlet.Servlet servlet;
	private boolean loadOnStartup;
	private int initOrder;
	private final String[] urlPattern;
	private final Map<String, String> initParams = new LinkedHashMap<>();

	public Servlet(Class<? extends javax.servlet.Servlet> clazz, String... urlPattern) {
		this.clazz = clazz;
		this.urlPattern = urlPattern;
	}

	public Servlet(javax.servlet.Servlet servlet, String... urlPattern) {
		this.servlet = servlet;
		this.urlPattern = urlPattern;
	}

	public Class<? extends javax.servlet.Servlet> getClazz() {
		return clazz;
	}

	public javax.servlet.Servlet getServlet() {
		return servlet;
	}

	public String[] getUrlPattern() {
		return urlPattern;
	}

	public Servlet setLoadOnStartup(boolean loadOnStartup) {
		this.loadOnStartup = loadOnStartup;
		return this;
	}

	public boolean isLoadOnStartup() {
		return loadOnStartup;
	}

	public Servlet setInitOrder(int initOrder) {
		this.initOrder = initOrder;
		return this;
	}

	public int getInitOrder() {
		return initOrder;
	}

	public Servlet addInitParam(String name, String value) {
		initParams.put(name, value);
		return this;
	}

	public Map<String, String> getInitParams() {
		return initParams;
	}

}
