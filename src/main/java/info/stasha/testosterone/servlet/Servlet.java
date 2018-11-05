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

	
	
	

}
