package info.stasha.testosterone.exceptionmapper;

import info.stasha.testosterone.DontIntercept;
import info.stasha.testosterone.JerseyRequestTest;
import info.stasha.testosterone.JerseyRequestTestRunner;
import org.junit.Test;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * Exception mapping provider test
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class ExceptionMappingProviderTest extends JerseyRequestTest {

	public static final String PATH = "exceptionMapping";

	public ExceptionMappingProviderTest() {
		this.configuration.register(ExceptionMapperProvider.class);
	}

	@GET
	@Path(PATH)
	@DontIntercept
	public String post() {
		throw new IllegalStateException();
	}

	@Test
	public void testmethod() {
		String resp = target().path(PATH).request().get().readEntity(String.class);
		assertEquals("ExceptionMappingProvider should return message", ExceptionMapperProvider.MESSAGE, resp);
	}

}
