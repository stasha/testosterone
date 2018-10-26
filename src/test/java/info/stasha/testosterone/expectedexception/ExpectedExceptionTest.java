package info.stasha.testosterone.expectedexception;

import info.stasha.testosterone.Testosterone;
import info.stasha.testosterone.jersey.JerseyRequestTestRunner;
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
@RunWith(JerseyRequestTestRunner.class)
public class ExpectedExceptionTest implements Testosterone {

	@Override
	public void configure(ResourceConfig config) {
		config.register(new AbstractBinder() {
			@Override
			protected void configure() {
				// Jersey 2.0 doesn't support "proxyForSameScope" method.
				this.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
			}
		});
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
