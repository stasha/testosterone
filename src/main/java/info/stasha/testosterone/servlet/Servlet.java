package info.stasha.testosterone.servlet;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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

	/**
	 * Creates new Servlet instance based on passed javax.servlet.Servlet class
	 * and url patterns.
	 *
	 * @param clazz
	 * @param urlPattern
	 */
	public Servlet(Class<? extends javax.servlet.Servlet> clazz, String... urlPattern) {
		this.clazz = clazz;
		this.urlPattern = urlPattern;
	}

	/**
	 * Creates new Servlet instance based on passed javax.servlet.Servlet
	 * instance and url patterns.
	 *
	 * @param servlet
	 * @param urlPattern
	 */
	public Servlet(javax.servlet.Servlet servlet, String... urlPattern) {
		this.servlet = servlet;
		this.urlPattern = urlPattern;
	}

	/**
	 * Returns registered javax.servlet.Servlet class.
	 *
	 * @return
	 */
	public Class<? extends javax.servlet.Servlet> getClazz() {
		return clazz;
	}

	/**
	 * Returns registered javax.servlet.Servlet instance.
	 *
	 * @return
	 */
	public javax.servlet.Servlet getServlet() {
		return servlet;
	}

	/**
	 * Returns registered url patterns.
	 *
	 * @return
	 */
	public String[] getUrlPattern() {
		return urlPattern;
	}

	/**
	 * Sets servlet load on startup.
	 *
	 * @param loadOnStartup
	 * @return
	 */
	public Servlet setLoadOnStartup(boolean loadOnStartup) {
		this.loadOnStartup = loadOnStartup;
		return this;
	}

	/**
	 * Returns true/false if servlet is loaded on server startup.
	 *
	 * @return
	 */
	public boolean isLoadOnStartup() {
		return loadOnStartup;
	}

	/**
	 * Sets servlet init order.
	 *
	 * @param initOrder
	 * @return
	 */
	public Servlet setInitOrder(int initOrder) {
		this.initOrder = initOrder;
		return this;
	}

	/**
	 * Returns servlet init order.
	 *
	 * @return
	 */
	public int getInitOrder() {
		return initOrder;
	}

	/**
	 * Adds servlet init param.
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public Servlet addInitParam(String name, String value) {
		initParams.put(name, value);
		return this;
	}

	/**
	 * Returns servlet init params.
	 *
	 * @return
	 */
	public Map<String, String> getInitParams() {
		return initParams;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + Objects.hashCode(this.clazz);
		hash = 89 * hash + Objects.hashCode(this.servlet);
		hash = 89 * hash + (this.loadOnStartup ? 1 : 0);
		hash = 89 * hash + this.initOrder;
		hash = 89 * hash + Arrays.deepHashCode(this.urlPattern);
		hash = 89 * hash + Objects.hashCode(this.initParams);
		return hash;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Servlet other = (Servlet) obj;
		if (this.loadOnStartup != other.loadOnStartup) {
			return false;
		}
		if (this.initOrder != other.initOrder) {
			return false;
		}
		if (!Objects.equals(this.clazz, other.clazz)) {
			return false;
		}
		if (!Objects.equals(this.servlet.getClass().getName(), other.servlet.getClass().getName())) {
			return false;
		}
		if (!Arrays.deepEquals(this.urlPattern, other.urlPattern)) {
			return false;
		}
		if (!Objects.equals(this.initParams, other.initParams)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Servlet{" + "clazz=" + clazz + ", servlet=" + servlet + ", loadOnStartup=" + loadOnStartup + ", initOrder=" + initOrder + ", urlPattern=" + urlPattern + ", initParams=" + initParams + '}';
	}

}
