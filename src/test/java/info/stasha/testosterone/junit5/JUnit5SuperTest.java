package info.stasha.testosterone.junit5;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.junit4.jersey.service.Service;
import info.stasha.testosterone.junit4.jersey.service.ServiceFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author stasha
 */
@Configuration(startServer = StartServer.PER_CLASS)
public class JUnit5SuperTest implements Testosterone {
	
	@Override
	public void configure(AbstractBinder binder) {
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@Context
	protected Service service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("beforeAll");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		System.out.println("afterAll");
	}

	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("BeforeEach");
	}

	@AfterEach
	public void tearDown() throws Exception {
		System.out.println("AfterEach");
	}

	@Test
	public void classInjectionTest() {
		assertEquals(Service.RESPONSE_TEXT, service.getText());
	}

	@Test
	public void methodInjectionTest(@Context Service service) {
		assertEquals(Service.RESPONSE_TEXT, service.getText());
	}

	@Test
	@Request(url = "404", expectedStatus = 404)
	public void requestTest(Response resp) {
		assertEquals(404, resp.getStatus());
	}
	
}
