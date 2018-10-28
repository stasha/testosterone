/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
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
//@Configuration(JerseyConfiguration.class)
//@Configuration(JettyConfiguration.class)
public class PlaygroundTest implements Testosterone {
	

		@Before
	public void before() {
		System.out.println("before");
	}

	@GET
	@Path("req")
	public void req() {
		Assert.fail("failed manually");
		System.out.println("req");
	}

	@Test(expected = AssertionError.class)
	@GET
	@Path("test")
	public void test() {
		System.out.println("test");
		target("req").request().get();
	}
}
