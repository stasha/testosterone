package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jerseyon.JerseyConfiguration;
import info.stasha.testosterone.jerseyon.Testosterone;
import info.stasha.testosterone.jerseyon.TestosteroneRunner;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(JerseyConfiguration.class)
//@Configuration(JettyConfiguration.class)
public class Playground implements Testosterone {


	@GET
	@Path("req")
	public void req() {
		System.out.println("req");
	}

	@Test
	@GET
	@Path("test")
	public void test() {
		System.out.println("test");
		target("req").request().get();
	}
}
