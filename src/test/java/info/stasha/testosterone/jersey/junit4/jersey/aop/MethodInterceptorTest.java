package info.stasha.testosterone.jersey.junit4.jersey.aop;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author stasha
 */
@Ignore
@RunWith(TestosteroneRunner.class)
public class MethodInterceptorTest implements Testosterone {

    @Path("/intercept")
    public static class MyInterceptedResource {

        @GET
        public String intercepted() {
            return "string";
        }
    }

    @Override
    public void configure(ResourceConfig config) {
        config.register(MyInterceptedResource.class);
    }

    @Override
    public void configure(AbstractBinder binder) {
        binder.bind(MyMethodInterceptor.class).to(InterceptionService.class).in(Singleton.class);
    }

    @Test
    @Request(url = "intercept")
    public void test(Response resp) {
        String result = resp.readEntity(String.class);
        assertEquals("Response should be intercepted", "intercepted string", result);
    }
}
