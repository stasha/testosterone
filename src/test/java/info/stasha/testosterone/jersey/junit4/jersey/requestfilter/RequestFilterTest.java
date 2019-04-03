package info.stasha.testosterone.jersey.junit4.jersey.requestfilter;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.Test;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;

/**
 * Filter test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class RequestFilterTest implements Testosterone {

    public static final String PATH = "filterChangedMethod";

    @Override
    public void configure(ResourceConfig config) {
        config.register(RequestFilter2.class);
        config.register(RequestFilter.class);
    }

    @POST
    @Path(PATH)
    public String post() {
        return "success";
    }

    @Test
    @Request(url = PATH)
    public void testmethod(Response resp) {
        // executing GET request that should be changed to POST by filter
        String result = resp.readEntity(String.class);
        assertEquals("Filter should change request method", "success", result);
    }

}
