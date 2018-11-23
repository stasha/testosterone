package info.stasha.testosterone.servlet;

import info.stasha.testosterone.Instantiable;
import info.stasha.testosterone.Utils;
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
public class Filter implements Instantiable {

    private Class<? extends javax.servlet.Filter> clazz;
    private javax.servlet.Filter filter;
    private String[] urlPattern;
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
     * Returns javax.servlet.Filter instance.
     *
     * @return
     */
    public javax.servlet.Filter getFilter() {
        return filter;
    }

    /**
     * Returns all registered url patterns for the filter.
     *
     * @return
     */
    public String[] getUrlPattern() {
        return urlPattern;
    }

    public Filter setUrlPatterns(String... patterns) {
        this.urlPattern = patterns;
        return this;
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
    public javax.servlet.Filter newInstance() {
        if (this.getFilter() != null) {
            return getFilter();
        }
        return Utils.<javax.servlet.Filter>newInstance(getClazz());
    }

    @Override
    public String toString() {
        return "Filter{" + "clazz=" + clazz + ", filter=" + filter + ", urlPattern=" + urlPattern + ", initParams=" + initParams + ", dispatchers=" + dispatchers + '}';
    }

}
