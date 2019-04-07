package info.stasha.testosterone.jersey.junit4.jersey.async;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ManagedAsync;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ManagedAsyncTest implements Testosterone {

    private static final String RESPONSE_TEXT = "resumed_text";

    @Override
    public void configure(ResourceConfig config) {
        config.register(ApplicationListener.class);
    }

    @GET
    @Path("/async")
    @ManagedAsync
    public void async(@Suspended AsyncResponse ac) throws InterruptedException {
        ac.resume(RESPONSE_TEXT);
    }

    @GET
    @Path("/asyncTimeout")
    @ManagedAsync
    public void timeout(@Suspended AsyncResponse ac) throws InterruptedException {
        Thread.sleep(1000);
    }

    @Test
    @Request(url = "async")
    public void asyncTest(Response resp) {
        assertEquals("Response text shold equal", RESPONSE_TEXT, resp.readEntity(String.class));
    }

    @Test
    @Request(url = "asyncTimeout", expectedStatus = 503)
    public void timeoutTest(Response resp) {
        assertEquals("Response status should equal", 503, resp.getStatus());
    }
}
