package info.stasha.testosterone;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author stasha
 */
@Path("helloworld")
public class HelloWorldResource {

	public static final String MESSAGE = "Hello World!";

	@GET
	@Produces("text/plain")
	public String getMessage() {
		return MESSAGE;
	}

}
