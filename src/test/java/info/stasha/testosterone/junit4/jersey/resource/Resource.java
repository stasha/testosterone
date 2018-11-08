package info.stasha.testosterone.jersey.resource;

import info.stasha.testosterone.jersey.service.Service;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * Simple hello world resource class
 *
 * @author stasha
 */
@Path(Resource.HELLO_WORLD_PATH)
public class Resource {

	public static final String HELLO_WORLD_PATH = "helloworld";
	public static final String SERVICE_PATH = HELLO_WORLD_PATH + "/service";
	public static final String MESSAGE = "Hello World!";

	@Context
	Service service;

	@GET
	@Produces("text/plain")
	public String getMessage() {
		System.out.println(MESSAGE);
		return MESSAGE;
	}

	@GET
	@Path("service")
	public String serviceMessage() {
		System.out.println(Service.RESPONSE_TEXT);
		return service.getText();
	}

}
