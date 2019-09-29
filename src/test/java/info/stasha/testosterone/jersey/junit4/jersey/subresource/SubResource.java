package info.stasha.testosterone.jersey.junit4.jersey.subresource;

import javax.ws.rs.GET;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ManagedAsync;

/**
 *
 * @author stasha
 */
public class SubResource {

    @GET
    @ManagedAsync
    public void get(@Suspended AsyncResponse resp) {
        resp.resume(Response.ok("hello get").build());
    }

    

}
