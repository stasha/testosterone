package info.stasha.testosterone.jersey.junit4.jersey.requestfilter;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;

/**
 * Request filter
 *
 * @author stasha
 */
@PreMatching
@Priority(2)
public class RequestFilter2 implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext crc) throws IOException {
		if (crc.getUriInfo().getPath().endsWith(RequestFilterTest.PATH)) {
			crc.setMethod("POST");
		}
	}

	@Override
	public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) throws IOException {

	}

}
