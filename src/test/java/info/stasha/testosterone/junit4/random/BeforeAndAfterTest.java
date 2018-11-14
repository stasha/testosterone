package info.stasha.testosterone.junit4.random;

import info.stasha.testosterone.junit4.*;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.jersey.service.Service;
import info.stasha.testosterone.junit4.jersey.service.ServiceFactory;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing @Before and @After if they are in requested scope
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class BeforeAndAfterTest implements Testosterone {

    private static int beforeCount = 0;
    private static int afterCount = 0;

    @Context
    Service service;

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @AfterClass
    public static void afterClass() {
        assertEquals("Before count should equal", 2, beforeCount);
        assertEquals("After count should equal", 2, afterCount);
    }

    @Before
    public void before() {
        assertEquals("Service should produce text", Service.RESPONSE_TEXT, service.getText());
        beforeCount++;
    }

    @Before
    public void before2() {
        assertEquals("Service should produce text", Service.RESPONSE_TEXT, service.getText());
        beforeCount++;
    }

    @After
    public void after() {
        assertEquals("Service should produce text", Service.RESPONSE_TEXT, service.getText());
        afterCount++;
    }

    @After
    public void after2() {
        assertEquals("Service should produce text", Service.RESPONSE_TEXT, service.getText());
        afterCount++;
    }

    @Test
    public void test() {
        System.out.println("test");
    }

}
