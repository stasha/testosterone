package info.stasha.testosterone.junit4.jersey.injectables;

import info.stasha.testosterone.junit4.*;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.jersey.service.Service;
import info.stasha.testosterone.junit4.jersey.service.ServiceFactory;
import info.stasha.testosterone.junit4.jersey.service.ServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import org.mockito.Spy;

/**
 * Mock and Spy Injection test.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class MockAndSpyInjectTest implements Testosterone {

    @Mock
    private Service service;

    @Spy
    private Service spyService;

    @Spy
    private ServiceImpl spyServiceImpl;

    @Override
    public void configureMocks(AbstractBinder binder) {
        binder.bind(ServiceImpl.class).to(ServiceImpl.class).in(RequestScoped.class).proxy(true);
        binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @Test
    public void testMockService() {
        assertNotNull("Service should be mock", service);
        assertNull("Service should return null", service.getText());
        Mockito.verify(service, times(1)).getText();
    }

    @Test
    public void testSpyFactoryService() {
        assertNotNull("Service should be mock", spyService);
        assertEquals("Service should return text", Service.RESPONSE_TEXT, spyService.getText());
        Mockito.verify(spyService, times(1)).getText();
    }

    @Test
    public void testSpyServiceImpl() {
        assertNotNull("Service should be mock", spyServiceImpl);
        assertEquals("Service should return text", Service.RESPONSE_TEXT, spyServiceImpl.getText());
        Mockito.verify(spyServiceImpl, times(1)).getText();

    }

}
