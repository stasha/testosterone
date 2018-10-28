package info.stasha.testosterone.jersey.requestfilter;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit.TestosteroneRunner;
import org.junit.Test;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * Filter test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class RequestFilterTest implements Testosterone {

	public static final String PATH = "filterChangedMethod";

	@Override
	public void configure(ResourceConfig config) {
		config.registerInstances(new RequestFilter());
	}

	@POST
	@Path(PATH)
	public String post() {
		return "success";
	}

	@Test
	public void testmethod() {
		// executing GET request that should be changed to POST by filter
		String resp = target().path(PATH).request().get().readEntity(String.class);
		assertEquals("Filter should change request method", "success", resp);
	}

}
