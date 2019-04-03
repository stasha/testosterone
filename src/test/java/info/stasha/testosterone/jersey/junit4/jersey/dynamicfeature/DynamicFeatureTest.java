package info.stasha.testosterone.jersey.junit4.jersey.dynamicfeature;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class DynamicFeatureTest implements Testosterone {

    @Override
    public void configure(ResourceConfig config) {
        config.register(MyDynamicFeature.class);
    }

    @GET
    @Path("/home")
    public String getDynamic() {
        return "test";
    }

    @Test
    @Request(url = "/home", expectedStatus = 502) // response code from dynamic feature
    public void dynamicFeatureTest(Response resp) {
    }

}
