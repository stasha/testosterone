package info.stasha.testosterone.testng;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.junit4.jersey.service.Service;
import info.stasha.testosterone.junit4.jersey.service.ServiceFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Configuration(serverStarts = StartServer.PER_CLASS)
public class SuperTest implements Testosterone {

	@Override
	public void configure(AbstractBinder binder) {
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}
	

	@Context
	private Service service;

	@BeforeClass
	static void setUpBeforeClass() throws Exception {
		System.out.println("beforeAll");
	}

	@AfterClass
	static void tearDownAfterClass() throws Exception {
		System.out.println("afterAll");
	}

	@Test
	public void classInjectionTest() {
		assertEquals(Service.RESPONSE_TEXT, service.getText());
	}
	
	@Test
	public void classInjectionTest2() {
		assertEquals(Service.RESPONSE_TEXT, service.getText());
	}
	
	@Test
	public void classInjectionTest3() {
		assertEquals(Service.RESPONSE_TEXT, service.getText());
	}

	@Test
	@Parameters("*")
	public void methodInjectionTest(@Optional @Context Service service) {
		assertEquals(Service.RESPONSE_TEXT, service.getText());
	}

	@Test
	@Parameters("*")
	@Request(url = "404", expectedStatus = 404)
	public void requestTest(@Optional Response resp) {
		assertEquals(404, resp.getStatus());
	}

}
