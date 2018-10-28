package info.stasha.testosterone.jersey.beanparam;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.Test;
import info.stasha.testosterone.jersey.service.User;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * BeanParam test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class BeanParamTest implements Testosterone {

	private static final String FIRST_NAME = "Jon";
	private static final String LAST_NAME = "Doe";
	private static final String STATE = "sleeping";

	@GET
	@Path("beanParam/{state}")
	public void beanParams(@BeanParam User user) {
		assertEquals("First Name should equal", FIRST_NAME, user.getFirstName());
		assertEquals("Last Name should equal", LAST_NAME, user.getLastName());
		assertEquals("State should equal", STATE, user.getState());
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
