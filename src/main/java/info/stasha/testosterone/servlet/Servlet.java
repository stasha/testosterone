package info.stasha.testosterone.servlet;

import info.stasha.testosterone.Instantiable;
import info.stasha.testosterone.Utils;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Testosterone representation of javax.servlet.Servlet
 *
 * @author stasha
 */
public class Servlet implements Instantiable {

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
    public javax.servlet.Servlet newInstance() {
        if (getServlet() != null) {
            return getServlet();
        }
        return Utils.<javax.servlet.Servlet>newInstance(getClazz());
    }

    @Override
    public String toString() {
        return "Servlet{" + "clazz=" + clazz + ", servlet=" + servlet + ", loadOnStartup=" + loadOnStartup + ", initOrder=" + initOrder + ", urlPattern=" + urlPattern + ", initParams=" + initParams + '}';
    }

}
