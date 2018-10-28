package info.stasha.testosterone.jersey.exceptionmapper;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.annotation.DontIntercept;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.Test;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * Exception mapping provider test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ExceptionMappingProviderTest implements Testosterone {

	public static final String PATH = "exceptionMapping";

	@Override
	public void configure(ResourceConfig config) {
		config.register(ExceptionMapperProvider.class);
	}

	@GET
	@Path(PATH)
	@DontIntercept
	public String post() {
		throw new IllegalStateException("!!! EXPECTED EXCEPTION - DON'T PANIC !!!");
	}

	@Test
	public void testmethod() {
		String resp = target().path(PATH).request().get().readEntity(String.class);
		assertEquals("ExceptionMappingProvider should return message", ExceptionMapperProvider.MESSAGE, resp);
	}

}
