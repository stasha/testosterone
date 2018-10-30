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
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import info.stasha.testosterone.jersey.service.Service;
import info.stasha.testosterone.jersey.service.ServiceImpl;
import javax.ws.rs.core.Context;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
//@Configuration(JerseyConfiguration.class)
//@Configuration(JettyConfiguration.class)
public class PlaygroundTest implements Testosterone {

	@Override
	public void configure(AbstractBinder binder) {
		binder.bindFactory(GenericMockitoFactory.mock(ServiceImpl.class)).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@Context
	private Service service;

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
		System.out.println(service.getText());
		Mockito.verify(service, times(1)).getText();
		target("req").request().get();
	}
}
