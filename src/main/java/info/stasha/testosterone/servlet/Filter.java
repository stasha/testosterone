package info.stasha.testosterone.servlet;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
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

	/**
	 * Creates Filter instance based on passed Class and url patterns.
	 *
	 * @param clazz
	 * @param urlPattern
	 */
	public Filter(Class<? extends javax.servlet.Filter> clazz, String... urlPattern) {
		this.clazz = clazz;
		this.urlPattern = urlPattern;
	}

	/**
	 * Creates Filter instance based on passed javax.servlet.Filter instance and
	 * url patterns.
	 *
	 * @param filter
	 * @param urlPattern
	 */
	public Filter(javax.servlet.Filter filter, String... urlPattern) {
		this.filter = filter;
		this.urlPattern = urlPattern;
	}

	/**
	 * Returns javax.servlet.Filter class.
	 *
	 * @return
	 */
	public Class<? extends javax.servlet.Filter> getClazz() {
		return clazz;
	}

	/**
	 * Sets javax.servlet.Filter class.
	 *
	 * @param clazz
	 */
	public void setClazz(Class<? extends javax.servlet.Filter> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Returns javax.servlet.Filter instance.
	 *
	 * @return
	 */
	public javax.servlet.Filter getFilter() {
		return filter;
	}

	/**
	 * Sets javax.servlet.Filter instance.
	 *
	 * @param filter
	 */
	public void setFilter(javax.servlet.Filter filter) {
		this.filter = filter;
	}

	/**
	 * Returns all registered url patterns for the filter.
	 *
	 * @return
	 */
	public String[] getUrlPattern() {
		return urlPattern;
	}

	/**
	 * Returns javax.servlet.Filter init params.
	 *
	 * @return
	 */
	public Map<String, String> getInitParams() {
		return initParams;
	}

	/**
	 * Returns javax.servlet.Filter dispatchers.
	 *
	 * @return
	 */
	public EnumSet<DispatcherType> getDispatchers() {
		return dispatchers;
	}

	/**
	 * Sets javax.servlet.Filter dispatchers.
	 *
	 * @param dispatchers
	 * @return
	 */
	public Filter setDispatchers(EnumSet<DispatcherType> dispatchers) {
		this.dispatchers = dispatchers;
		return this;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + Objects.hashCode(this.clazz);
		hash = 29 * hash + Objects.hashCode(this.filter);
		hash = 29 * hash + Arrays.deepHashCode(this.urlPattern);
		hash = 29 * hash + Objects.hashCode(this.initParams);
		hash = 29 * hash + Objects.hashCode(this.dispatchers);
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
		final Filter other = (Filter) obj;
		if (!Objects.equals(this.clazz, other.clazz)) {
			return false;
		}
		if (!Objects.equals(this.filter, other.filter)) {
			return false;
		}
		if (!Arrays.deepEquals(this.urlPattern, other.urlPattern)) {
			return false;
		}
		if (!Objects.equals(this.initParams, other.initParams)) {
			return false;
		}
		if (!Objects.equals(this.dispatchers, other.dispatchers)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Filter{" + "clazz=" + clazz + ", filter=" + filter + ", urlPattern=" + urlPattern + ", initParams=" + initParams + ", dispatchers=" + dispatchers + '}';
	}

}
