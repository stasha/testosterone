package info.stasha.testosterone.junit5;

import info.stasha.testosterone.jersey.service.Service;
import info.stasha.testosterone.jersey.service.ServiceFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Disabled;

public class JUnit5Test implements Testosterone {

	@Override
	public void configure(AbstractBinder binder) {
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}
	
	@Context 
	private Service service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
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
		assertEquals("Returned message should equal", Service.RESPONSE_TEXT, service.getText());
	}
	
	@Test
	public void methodInjectionTest(@Context Service service) {
		assertEquals("Returned message should equal", Service.RESPONSE_TEXT, service.getText());
	}

}
