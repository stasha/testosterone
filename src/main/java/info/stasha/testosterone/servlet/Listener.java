package info.stasha.testosterone.servlet;

import java.util.EventListener;

/**
 * Testosterone representation of servlet EventListener.
 *
 * @author stasha
 */
public class Listener {

    private Class<? extends EventListener> clazz;
    private EventListener listener;

    /**
     * Creates new Listener instance based on passed EventListener class.
     *
     * @param listener
     */
    public Listener(Class<? extends EventListener> listener) {
        this.clazz = listener;
    }

    /**
     * Creates new Listener instance based on passed EventListener instance.
     *
     * @param listener
     */
    public Listener(EventListener listener) {
        this.listener = listener;
    }

    /**
     * Returns EventListener class.
     *
     * @return
     */
    public Class<? extends EventListener> getClazz() {
        return clazz;
    }

    /**
     * Returns EventListener instance.
     *
     * @return
     */
    public EventListener getListener() {
        return listener;
    }

}
