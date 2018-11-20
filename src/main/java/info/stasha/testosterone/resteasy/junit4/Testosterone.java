package info.stasha.testosterone.resteasy.junit4;

import com.google.inject.Binder;
import info.stasha.testosterone.SuperTestosterone;

/**
 * Testosterone
 *
 * @author stasha
 */
public interface Testosterone extends SuperTestosterone {

    /**
     * Override to configureMocks AbstractBinder.
     *
     * @param binder
     */
    default void configure(Binder binder) {

    }

    /**
     * Override to configureMocks MockingAbstractBinder.<br>
     * This method is not invoked when Test class is @Integration.
     *
     * @param binder
     */
    default void configureMocks(Binder binder) {

    }

}
