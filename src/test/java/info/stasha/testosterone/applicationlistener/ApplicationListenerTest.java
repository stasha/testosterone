package info.stasha.testosterone.applicationlistener;

import info.stasha.testosterone.jersey.JerseyRequestTest;
import info.stasha.testosterone.jersey.JerseyRequestTestRunner;
import org.junit.Test;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Application event listener test<br>
 *
 * NOTE ApplicationEventListener is supported from Jersey version 2.1
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class ApplicationListenerTest extends JerseyRequestTest {

	private static final ApplicationListener APPLICATION_LISTENER = Mockito.spy(new ApplicationListener());

	@Override
	protected void init() {
		this.configuration.registerInstances(APPLICATION_LISTENER);
	}

	@AfterClass
	public static void afterClass() {
		// jersey version 2.1, 2.2, 2.3, 2.4 will invoke only 3 times
		Mockito.verify(APPLICATION_LISTENER, times(4)).onEvent(Mockito.any(ApplicationEvent.class));
	}

	@Test
	public void applicationListenerTest() {
		// jersey version 2.1, 2.2, 2.3, 2.4 will invoke only 2 times
		Mockito.verify(APPLICATION_LISTENER, times(3)).onEvent(Mockito.any(ApplicationEvent.class));
		Mockito.verify(APPLICATION_LISTENER, times(1)).onRequest(Mockito.any(RequestEvent.class));
	}

}
