package info.stasha.testosterone.resource;

import info.stasha.testosterone.JerseyRequestTest;
import info.stasha.testosterone.JerseyRequestTestRunner;
import info.stasha.testosterone.RequestTest;
import info.stasha.testosterone.service.Service;
import info.stasha.testosterone.service.ServiceFactory;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.runner.RunWith;

/**
 * Resource test
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class ResourceTest extends JerseyRequestTest {

	public ResourceTest() {
		this.configuration.register(Resource.class);
		this.abstractBinder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@RequestTest
	public void testResource() {
		String resp = target().path(Resource.PATH).request().get().readEntity(String.class);
		assertEquals("Message returned by request should equal", Resource.MESSAGE, resp);
	}

	@RequestTest
	public void testResourceService() {
		String resp = target().path(Resource.PATH).path("service").request().get().readEntity(String.class);
		assertEquals("Message returned by request should equal", Service.RESPONSE_TEXT, resp);
	}

}
