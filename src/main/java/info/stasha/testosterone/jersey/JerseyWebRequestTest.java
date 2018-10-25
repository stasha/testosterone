package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Testosterone;
import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import org.junit.After;
import org.junit.Before;

import static org.glassfish.grizzly.http.CookiesBuilder.client;
import static org.glassfish.grizzly.http.CookiesBuilder.server;
// supported from Jersey 2.26
//import org.glassfish.jersey.internal.inject.AbstractBinder;

/**
 * This class sets up WEB container for running tests.
 *
 * @author stasha
 */
public class JerseyWebRequestTest implements Testosterone {

	private static final Logger LOGGER = Logger.getLogger(JerseyWebRequestTest.class.getName());



	private Throwable thrownException;

	@Override
	public Set<Throwable> getMessages() {
		return messages;
	}

	@Override
	public Throwable getThrownException() {
		return thrownException;
	}

	@Override
	public void setThrownException(Throwable thrownException) {
		this.thrownException = thrownException;
	}


	

}
