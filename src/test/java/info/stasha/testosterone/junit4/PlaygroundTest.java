package info.stasha.testosterone.junit4;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.junit4.jersey.resource.Resource;
import info.stasha.testosterone.junit4.jersey.service.Service;
import info.stasha.testosterone.junit4.jersey.service.Service2;
import info.stasha.testosterone.junit4.jersey.service.ServiceImpl;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 *
 * @author stasha
 */
@Configuration(serverStarts = Configuration.ServerStarts.PER_TEST, port = 9998)
public class PlaygroundTest extends SuperTest {

	@Override
	public void configure(ResourceConfig config) {
		config.register(Resource.class);
		config.register(Resource.class);
		config.register(Resource.class);
		config.register(Resource.class);
	}

	@Override
	public void configure(AbstractBinder binder) {
		binder.bindFactory(GenericMockitoFactory.mock(ServiceImpl.class)).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
		binder.bindFactory(GenericMockitoFactory.get(ServiceImpl.class)).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@BeforeClass
	public static void beforeClass() {
		System.out.println("internal before class");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("internal after class");
	}

	@Before
	public void before() {
		System.out.println("before");
	}

	@After
	public void after() {
		System.out.println("after");
	}

	@Context
	private Service service;

	@Context
	private Service2 service2;

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

	@Test(expected = AssertionError.class)
	@GET
	@Path("test2")
	public void test2() {
		System.out.println("test");
		System.out.println(service.getText());
		Mockito.verify(service, times(1)).getText();
		target("req").request().get();
	}
}
