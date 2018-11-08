package info.stasha.testosterone.junit4.jersey.requestfilter;

import java.io.IOException;
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
public class RequestFilter implements ContainerRequestFilter, ContainerResponseFilter {

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
