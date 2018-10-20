package info.stasha.testosterone.applicationlistener;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

/**
 * Application event listener
 *
 * @author stasha
 */
public class ApplicationListener implements ApplicationEventListener {

	@Override
	public void onEvent(ApplicationEvent ae) {
		// do nothing
	}

	@Override
	public RequestEventListener onRequest(RequestEvent re) {
		return null;
	}

}
