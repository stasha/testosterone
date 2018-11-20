package info.stasha.testosterone.jersey.junit4.jersey.exceptionmapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapping provider
 *
 * @author stasha
 */
@Provider
public class ExceptionMapperProvider implements ExceptionMapper<Throwable> {

	public static final String MESSAGE = "IllegalStateException occured";

	@Override
	public Response toResponse(Throwable e) {
		if (e instanceof IllegalStateException) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(MESSAGE).build();
		}

		return Response.serverError().build();
	}

}
