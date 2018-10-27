package info.stasha.testosterone.requestparams;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit.TestosteroneRunner;
import org.junit.Test;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * Request params test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class RequestParamsTest implements Testosterone {

	private static final String FIRST_NAME = "Jon";
	private static final String LAST_NAME = "Doe";
	private static final String STATE = "sleeping";

	@GET
	@Path("beanParam/{state}")
	public void beanParams(
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@PathParam("state") String state
	) {
		assertEquals("First Name should equal", FIRST_NAME, firstName);
		assertEquals("Last Name should equal", LAST_NAME, lastName);
		assertEquals("State should equal", STATE, state);
	}

	@Test
	public void testParams() {
		Response resp = target().path("beanParam").path(STATE)
				.queryParam("firstName", FIRST_NAME)
				.queryParam("lastName", LAST_NAME)
				.request().get();

		assertEquals("Status code should be 204", 204, resp.getStatus());
	}

}
