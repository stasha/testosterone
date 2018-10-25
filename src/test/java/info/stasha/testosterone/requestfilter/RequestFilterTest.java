package info.stasha.testosterone.requestfilter;

import info.stasha.testosterone.jersey.JerseyRequestTest;
import info.stasha.testosterone.jersey.JerseyRequestTestRunner;
import org.junit.Test;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * Filter test
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class RequestFilterTest extends JerseyRequestTest {

	public static final String PATH = "filterChangedMethod";

	@Override
	protected void init() {
		this.configuration.registerInstances(new RequestFilter());
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
