package info.stasha.testosterone.servlet;

import java.util.EventListener;
import java.util.Objects;

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
	 * Sets EventListener class.
	 *
	 * @param clazz
	 */
	public void setClazz(Class<? extends EventListener> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Returns EventListener instance.
	 *
	 * @return
	 */
	public EventListener getListener() {
		return listener;
	}

	/**
	 * Sets EventListener instance.
	 *
	 * @param listener
	 */
	public void setListener(EventListener listener) {
		this.listener = listener;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + Objects.hashCode(this.listener);
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
		final Listener other = (Listener) obj;
		return this.listener.getClass().getName().equals(other.getListener().getClass().getName());
	}

}
