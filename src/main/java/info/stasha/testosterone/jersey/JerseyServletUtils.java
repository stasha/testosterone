package info.stasha.testosterone.jersey;

import info.stasha.testosterone.servlet.Servlet;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Util for registering Jersey servlet.
 *
 * @author stasha
 */
public class JerseyServletUtils {

    private static final Logger LOGGER = Logger.getLogger(JerseyServletUtils.class.getName());

    public static void registerJerseyServlet(JerseyTestConfig root) {
        try {
            Constructor c = Class.forName("org.glassfish.jersey.servlet.ServletContainer").getConstructor(ResourceConfig.class);
            // registering jersey servlet
            Servlet s = new Servlet((javax.servlet.Servlet) c.newInstance(root.getApplication()),
                    root.getServletContainerConfig().getJaxRsPath()).setInitOrder(0);
            root.getServletContainerConfig().addServlet(s);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
            LOGGER.warning("org.glassfish.jersey.servlet.ServletContainer is missing");
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.warning("org.glassfish.jersey.servlet.ServletContainer is missing");
        }
    }
}
