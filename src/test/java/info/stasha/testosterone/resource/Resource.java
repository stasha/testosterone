package info.stasha.testosterone.resource;

import info.stasha.testosterone.service.Service;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * Simple hello world resource class
 *
 * @author stasha
 */
@Path(Resource.PATH)
public class Resource {

	public static final String PATH = "helloworld";
	public static final String MESSAGE = "Hello World!";

	@Context
	Service service;

	@GET
	@Produces("text/plain")
	public String getMessage() {
		return MESSAGE;
	}

	@GET
	@Path("service")
	public String serviceMessage() {
		return service.getText();
	}

}
