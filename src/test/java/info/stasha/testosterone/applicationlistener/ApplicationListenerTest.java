package info.stasha.testosterone.applicationlistener;

import info.stasha.testosterone.JerseyRequestTest;
import info.stasha.testosterone.JerseyRequestTestRunner;
import info.stasha.testosterone.RequestTest;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Application event listener test
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class ApplicationListenerTest extends JerseyRequestTest {

	private static final ApplicationListener APPLICATION_LISTENER = Mockito.spy(new ApplicationListener());

	public ApplicationListenerTest() {
		this.configuration.registerInstances(APPLICATION_LISTENER);
	}

	@AfterClass
	public static void afterClass() {
		Mockito.verify(APPLICATION_LISTENER, times(4)).onEvent(Mockito.any(ApplicationEvent.class));
	}

	@RequestTest
	public void applicationListenerTest() {
		Mockito.verify(APPLICATION_LISTENER, times(3)).onEvent(Mockito.any(ApplicationEvent.class));
		Mockito.verify(APPLICATION_LISTENER, times(1)).onRequest(Mockito.any(RequestEvent.class));
	}

}
