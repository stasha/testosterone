package info.stasha.testosterone.servlet;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.DispatcherType;
import static javax.servlet.DispatcherType.*;

/**
 * Testosterone representation of javax.servlet.Filter
 *
 * @author stasha
 */
public class Filter {

	private Class<? extends javax.servlet.Filter> clazz;
	private javax.servlet.Filter filter;
	private final String[] urlPattern;
	private final Map<String, String> initParams = new LinkedHashMap<>();
	private EnumSet<DispatcherType> dispatchers = EnumSet.of(ASYNC, ERROR, FORWARD, INCLUDE, REQUEST);

	public Filter(Class<? extends javax.servlet.Filter> clazz, String... urlPattern) {
		this.clazz = clazz;
		this.urlPattern = urlPattern;
	}

	public Filter(javax.servlet.Filter filter, String... urlPattern) {
		this.filter = filter;
		this.urlPattern = urlPattern;
	}

	public Class<? extends javax.servlet.Filter> getClazz() {
		return clazz;
	}

	public void setClazz(Class<? extends javax.servlet.Filter> clazz) {
		this.clazz = clazz;
	}

	public javax.servlet.Filter getFilter() {
		return filter;
	}

	public void setFilter(javax.servlet.Filter filter) {
		this.filter = filter;
	}

	public String[] getUrlPattern() {
		return urlPattern;
	}

	public Map<String, String> getInitParams() {
		return initParams;
	}

	public EnumSet<DispatcherType> getDispatchers() {
		return dispatchers;
	}

	public Filter setDispatchers(EnumSet<DispatcherType> dispatchers) {
		this.dispatchers = dispatchers;
		return this;
	}

}
