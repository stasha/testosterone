package info.stasha.testosterone.expectedexception;

import info.stasha.testosterone.TestosteroneRunner;
import info.stasha.testosterone.TestosteroneWithAbstractBinder;
import info.stasha.testosterone.service.Service;
import info.stasha.testosterone.service.ServiceFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Expected exception test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ExpectedExceptionTest implements TestosteroneWithAbstractBinder {

	@Override
	public void configure(ResourceConfig config, AbstractBinder binder) {
		// Jersey 2.0 doesn't support "proxyForSameScope" method.
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);

	}

	@Test(expected = IllegalStateException.class)
	public void illegalStateExceptionTest(@Context Service service) {
		service.throwIllegalStateException();
	}

	@GET
	@Path("exceptionTest")
	public String throwIllegalStateException(@Context Service service) {
		service.throwIllegalStateException();
		return "not reachable";
	}

	@Test(expected = IllegalStateException.class)
	public void illegalStateExceptionByRequestTest() {
		target().path("exceptionTest").request().get();
	}

}
