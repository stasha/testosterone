package info.stasha.testosterone.junit4;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.jersey.resource.Resource;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ResourceTestInSuite implements Testosterone {

	@Test
	@Request(url = Resource.HELLO_WORLD_PATH)
	public void helloWorldTest(Response resp) {
		System.out.println(resp);
		assertEquals("Messages should equal", Resource.MESSAGE, resp.readEntity(String.class));
	}

}
