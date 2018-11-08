package info.stasha.testosterone.junit4.jersey.applicationlistener;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

/**
 * Application event listener<br>
 *
 * NOTE ApplicationEventListener is supported from Jersey version 2.1
 *
 * @author stasha
 */
public class ApplicationListener implements ApplicationEventListener {

	@Override
	public void onEvent(ApplicationEvent ae) {
		// do nothing
		System.out.println("ApplicationEvent: " + ae.getType());
	}

	@Override
	public RequestEventListener onRequest(RequestEvent re) {
		System.out.println("Request event: " + re.getType());
		return null;
	}

}
