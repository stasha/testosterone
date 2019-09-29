package info.stasha.testosterone.jersey.junit4.jersey.subresource;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author stasha
 */
@Path("main")
public class MainResource {

    @Path("/")
    public Class<?> get(@QueryParam("type") @DefaultValue("") String type) {
        switch (type) {
            case "test2":
                return SubResource2.class;
            default:
                return SubResource.class;
        }
    }

}
