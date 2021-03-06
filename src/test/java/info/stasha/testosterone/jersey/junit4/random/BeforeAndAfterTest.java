package info.stasha.testosterone.jersey.junit4.random;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.jersey.junit4.jersey.service.Service;
import info.stasha.testosterone.jersey.junit4.jersey.service.ServiceFactory;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.After;
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

    @Override
    public void afterServerStop() {
        // There are two tests, 2x @Before/@After annotated methods and 
        // one @Request annotated test method. @Request means 1 more test 
        // instantiation which means all methods with @Before and @After will
        // be invoked again, so in this case 2x each.
        assertEquals("Before count should equal", 6, beforeCount);
        assertEquals("After count should equal", 6, afterCount);
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
        assertEquals("Service should produce text", Service.RESPONSE_TEXT, service.getText());
    }

    @Test
    @Request
    public void test2(Response resp) {
        assertEquals("Service should produce text", Service.RESPONSE_TEXT, service.getText());
        System.out.println(resp);
    }

}
