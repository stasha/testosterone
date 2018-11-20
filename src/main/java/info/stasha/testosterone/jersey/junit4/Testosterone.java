package info.stasha.testosterone.jersey.junit4;

import info.stasha.testosterone.SuperTestosterone;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Testosterone
 *
 * @author stasha
 */
public interface Testosterone extends SuperTestosterone {

    /**
     * Override to configureMocks ResourceConfig.
     *
     * @param config
     */
    default void configure(ResourceConfig config) {

    }

    /**
     * Override to configureMocks ResourceConfig.
     *
     * @param config
     */
    default void configureMocks(ResourceConfig config) {

    }

    /**
     * Override to configureMocks AbstractBinder.
     *
     * @param binder
     */
    default void configure(AbstractBinder binder) {

    }

    /**
     * Override to configureMocks MockingAbstractBinder.<br>
     * This method is not invoked when Test class is @Integration.
     *
     * @param binder
     */
    default void configureMocks(AbstractBinder binder) {

    }

}
