package info.stasha.testosterone.cdi.weld;

import org.jboss.weld.proxy.WeldClientProxy;

/**
 * Weld utils.
 *
 * @author stasha
 */
public class WeldUtils {

    public WeldUtils() {
    }

    /**
     * Unwraps real instance from weld proxy object.
     *
     * @param <T>
     * @param obj
     * @return
     */
    public static <T> T unwrap(T obj) {
        try {
            return (T) ((WeldClientProxy) obj).getMetadata().getContextualInstance();
        } catch (ClassCastException ex) {
            // it's not wrapped
            return obj;
        }
    }
}
