package info.stasha.testosterone;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.AdditionalAnswers.delegatesTo;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class HelloWorldTest extends MyAppSuperTest {

	private static final String INTERNAL_RESOURCE_PATH = "internalTestResourceMethod";

	private MyService myService;

	@Context
	public void setMyService(MyService myService) {
		this.myService = Mockito.mock(MyService.class, delegatesTo(myService));
	}

	@RequestTest(expected = RuntimeException.class)
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
//		assertTrue("should be true", false);

		String text = myService.getText();
		Mockito.verify(myService, times(1)).getText();

		return text;
	}

	@POST
	@Path("test")
	@RequestTest(entity = "my entity")
	public void testInternalTestResourceMethod(String postData) {
		assertEquals("Text should equal", "my entity", postData);
		Mockito.verify(myService, times(0)).getText();

		Response resp = target().path(INTERNAL_RESOURCE_PATH).request().get();
		String data = resp.readEntity(String.class);
		assertEquals("Messages should equal", MyService.RESPONSE_TEXT, data);

		Mockito.verify(myService, times(1)).getText();
	}

	@RequestTest
	public void testHelloWorldResource() {
		Response resp = target().path("helloworld").request().get();
		String data = resp.readEntity(String.class);
		assertEquals("Messages should equal", HelloWorldResource.MESSAGE, data);
	}

	@RequestTest
	public void test2() {
		System.out.println(myService.getText());
	}

	@Test
	public void test3() {
//		assertTrue(true);
		testAssert();

	}

	private void testAssert() {
		assertTrue("Should be true", true);
	}

}
