package info.stasha.testosterone.service;

import info.stasha.testosterone.JerseyRequestTest;
import info.stasha.testosterone.JerseyRequestTestRunner;
import org.junit.Test;
import javax.ws.rs.core.Context;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.runner.RunWith;
import static org.mockito.AdditionalAnswers.delegatesTo;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Service test
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class ServiceTest extends JerseyRequestTest {

	private Service service;

	public ServiceTest() {
		this.abstractBinder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@Context
	public void setMyService(Service service) {
		this.service = Mockito.mock(Service.class, delegatesTo(service));
	}

	@Test
	public void messageTest() {
		Mockito.verify(service, times(0)).getText();
		assertEquals("Returned message should equal", Service.RESPONSE_TEXT, service.getText());
		Mockito.verify(service, times(1)).getText();
	}

	@Test
	public void zeroInteractionsTest() {
		Mockito.verifyZeroInteractions(service);
	}

	@Test
	public void customReturnTest() {
		assertNull("User should be null", service.getUser());
		Mockito.doReturn(new User()).when(service).getUser();
		assertNotNull("User should be not null", service.getUser());
	}
}
