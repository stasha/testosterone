package info.stasha.testosterone.resource;

import info.stasha.testosterone.jersey.JerseyRequestTest;
import info.stasha.testosterone.jersey.JerseyRequestTestRunner;
import org.junit.Test;
import info.stasha.testosterone.service.Service;
import info.stasha.testosterone.service.ServiceFactory;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * Resource test
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class ResourceTest extends JerseyRequestTest {

	@Override
	protected void init() {
		this.configuration.register(Resource.class);
		// Jersey 2.0 doesn't support "proxyForSameScope" method.
		this.abstractBinder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@Test
	public void testResource() {
		String resp = target().path(Resource.HELLO_WORLD_PATH).request().get().readEntity(String.class);
		assertEquals("Message returned by request should equal", Resource.MESSAGE, resp);
	}

	@Test
	public void testResourceService() {
		String resp = target().path(Resource.HELLO_WORLD_PATH).path("service").request().get().readEntity(String.class);
		assertEquals("Message returned by request should equal", Service.RESPONSE_TEXT, resp);
	}

}
