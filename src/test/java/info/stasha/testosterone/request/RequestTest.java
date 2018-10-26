package info.stasha.testosterone.request;

import info.stasha.testosterone.jersey.JerseyRequestTest;
import info.stasha.testosterone.jersey.JerseyRequestTestRunner;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.annotation.Requests;
import info.stasha.testosterone.resource.Resource;
import info.stasha.testosterone.service.Service;
import info.stasha.testosterone.service.ServiceFactory;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Request test
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class RequestTest extends JerseyRequestTest {

	@Override
	public void configure(ResourceConfig config) {
		config.register(Resource.class);
		config.register(new AbstractBinder() {
			@Override
			protected void configure() {
				// Jersey 2.0 doesn't support "proxyForSameScope" method.
				this.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
			}

		});
	}

	/**
	 * Multi request test.
	 *
	 * @param service
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	@Test
	@GET
	@Path("test/{service}/{id}")
	@Requests(
			repeat = 2,
			requests = {
				@Request(url = "test/[a-z]{10,20}/[0-9]{1,10}?firstName=Jon&lastName=Doe")
				,
				@Request(url = "test2/car/[a-z]{10,20}", method = HttpMethod.POST, excludeFromRepeat = {2})
				,
				@Request(url = "test2/truck/[a-z]{10,20}", method = HttpMethod.POST)
			})
	public void multiRequestTest(
			@PathParam("service") String service,
			@PathParam("id") String id,
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName) {

		assertData(service, id, firstName, lastName);
	}

	/**
	 * This will be invoked by "annotations" placed on "multiRequestTest" method
	 *
	 * @param service
	 * @param id
	 */
	@POST
	@Path("test2/{service}/{id}")
	public void multiRequestTestExternalMethod(
			@PathParam("service") String service,
			@PathParam("id") String id) {

		assertTrue("Service should be car or truck", (service.equals("car") || service.equals("truck")));
		assertTrue("Id should be number", id != null && !id.isEmpty());
	}

	/**
	 * Single request test
	 *
	 * @param service
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	@Test
	@POST
	@Path("test/{service}/{id}")
	@Request(url = "test/[a-z]{10,20}/[0-9]{1,10}?firstName=Jon&lastName=Doe", method = HttpMethod.POST)
	public void singleRequestTest(
			@PathParam("service") String service,
			@PathParam("id") String id,
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName) {

		assertData(service, id, firstName, lastName);
	}

	/**
	 * Single request to external resource test
	 *
	 * @param response
	 */
	@Test
	@Request(url = Resource.HELLO_WORLD_PATH)
	public void singleRequestExternalResourceTest(Response response) {
		assertEquals("Status should equal", 200, response.getStatus());
		assertEquals("Response text should equal", Resource.MESSAGE, response.readEntity(String.class));
	}

	/**
	 * Multi request to external resource test
	 *
	 * @param response
	 */
	@Test
	@Requests(requests = {
		@Request(url = Resource.HELLO_WORLD_PATH)
		,
		@Request(url = Resource.SERVICE_PATH)
	})
	public void multiRequestExternalResourceTest(Response response) {
		assertEquals("Status should equal", 200, response.getStatus());

		String resp = response.readEntity(String.class);
		assertTrue("Message should be: ", Resource.MESSAGE.equals(resp) || Service.RESPONSE_TEXT.equals(resp));
	}

	/**
	 * Checks injected data.
	 *
	 * @param service
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	private void assertData(String service, String id, String firstName, String lastName) {
		assertTrue("Service should be string", service != null && !service.isEmpty());
		assertTrue("Id should be number", (Integer) Integer.parseInt(id) instanceof Integer);
		assertEquals("FirstName should equal", "Jon", firstName);
		assertEquals("LastName should equal", "Doe", lastName);
	}

}
