package info.stasha.testosterone.servlet;

import java.util.EventListener;
import java.util.Objects;

/**
 *
 * @author stasha
 */
public class Listener {

	private final EventListener listener;

	public Listener(EventListener listener) {
		this.listener = listener;
	}

	public EventListener getListener() {
		return listener;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + Objects.hashCode(this.listener);
		return hash;
	}

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
