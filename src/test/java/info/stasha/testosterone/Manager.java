package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.JerseyConfiguration;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.jersey.TestosteroneRunner;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class Manager implements Testosterone {


	@Test
	public void t() {
		Assert.fail("failed manually");
		System.out.println("test");
		target("req").request().get();
	}
}
