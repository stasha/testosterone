package info.stasha.testosterone.junit4.jersey.resource;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.Test;
import info.stasha.testosterone.junit4.jersey.service.Service;
import info.stasha.testosterone.junit4.jersey.service.ServiceFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * Resource test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ResourceTest implements Testosterone {

	@Override
	public void configure(ResourceConfig config) {
		config.register(Resource.class);
	}

	@Override
	public void configure(AbstractBinder binder) {
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
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
