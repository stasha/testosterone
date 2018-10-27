package info.stasha.testosterone;


import info.stasha.testosterone.resource.Resource;
import info.stasha.testosterone.service.Service;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.mockito.AdditionalAnswers.delegatesTo;
import org.mockito.Mockito;

public class HelloWorldTest extends MyAppSuperTest {

	private static final String INTERNAL_RESOURCE_PATH = "internalTestResourceMethod";

	private Service myService;

	@Context
	public void setService(Service myService) {
		this.myService = Mockito.mock(Service.class, delegatesTo(myService));
	}

	@Test(expected = IllegalStateException.class)
	public void test() {
		if (1 == 1) {
			throw new IllegalStateException("exception");
		}
		System.out.println(myService.getText());
	}

	@GET
	@Path(INTERNAL_RESOURCE_PATH)
	@Produces("text/plain")
	public String test1(@Context UriInfo uriInfo) {
		assertTrue(uriInfo.getRequestUri().toString().endsWith(INTERNAL_RESOURCE_PATH));
		return myService.getText();
	}

	@Test
	public void testHelloWorldResource() {
		Response resp = target().path("helloworld").request().get();
		String data = resp.readEntity(String.class);
		assertEquals("Messages should equal", Resource.MESSAGE, data);
	}

	@Test
	public void test2() {
		System.out.println(myService.getText());
	}

	@Test(expected = RuntimeException.class)
	public void test3() {
		assertTrue(true);

		if (1 == 1) {
			throw new IllegalStateException("exception");
		}
	}

}
