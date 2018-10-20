package info.stasha.testosterone.readwriteinterceptor;

import info.stasha.testosterone.JerseyRequestTest;
import info.stasha.testosterone.JerseyRequestTestRunner;
import info.stasha.testosterone.RequestTest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.junit.runner.RunWith;

/**
 * Interceptor test
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class ReadWriteInterceptorTest extends JerseyRequestTest {

	public static final String PATH = "changeTextInterceptor";

	public ReadWriteInterceptorTest() {
		this.configuration.registerInstances(new ReadWriteInterceptor());
	}

	@GET
	@Path(PATH)
	public String post() {
		return "success";
	}

	@RequestTest
	public void testmethod(String data) {
		// Interceptor should pass string data
		assertEquals("Interceptor should pass string data", ReadWriteInterceptor.READ_FROM_TEXT, data);

		// without interceptor, returned text should be "success"
		String resp = target().path(PATH).request().get().readEntity(String.class);

		// Text should be changed by interceptor
		assertEquals("Interceptor should return string data", ReadWriteInterceptor.WRITE_TO_TEXT, resp);
	}

}
