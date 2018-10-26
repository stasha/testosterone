package info.stasha.testosterone.service;

import info.stasha.testosterone.TestosteroneRunner;
import info.stasha.testosterone.TestosteroneWithAbstractBinder;
import org.junit.Test;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
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
@RunWith(TestosteroneRunner.class)
public class ServiceTest implements TestosteroneWithAbstractBinder {

	private Service service;

	@Override
	public void configure(ResourceConfig config, AbstractBinder binder) {

		// Jersey 2.0 doesn't support "proxyForSameScope" method.
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);

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
