package info.stasha.testosterone.jersey.junit4.jersey.subresource;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class SubResourceTest implements Testosterone {

    @Override
    public void configure(ResourceConfig config) {
        config.register(MainResource.class);
    }

    @Test
    @Request(url = "/main")
    public void test(Response resp) {
        assertEquals("Response text should equal", "hello get", resp.readEntity(String.class));
    }

    @Test
    @Request(url = "/main?type=test2")
    public void test2(Response resp) {
        assertEquals("Response text should equal", "hello get 2", resp.readEntity(String.class));
    }
}
