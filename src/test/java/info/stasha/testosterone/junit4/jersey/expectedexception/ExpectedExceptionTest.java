package info.stasha.testosterone.junit4.jersey.expectedexception;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.junit4.jersey.service.Service;
import info.stasha.testosterone.junit4.jersey.service.ServiceFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Expected exception test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ExpectedExceptionTest implements Testosterone {

	@Override
	public void configure(AbstractBinder binder) {
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@Test(expected = IllegalStateException.class)
	public void illegalStateExceptionTest(@Context Service service) {
		System.out.println("");
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
