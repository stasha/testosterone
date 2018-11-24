package info.stasha.testosterone.jersey.junit4.jersey.injectables;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.FactoryUtils;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.jersey.junit4.jersey.service.GreetService;
import info.stasha.testosterone.jersey.junit4.jersey.service.Service;
import info.stasha.testosterone.jersey.junit4.jersey.service.ServiceFactory;
import info.stasha.testosterone.jersey.junit4.jersey.service.ServiceImpl;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import org.mockito.Spy;

/**
 * Mock and Spy test.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class SpyInjectTest implements Testosterone {

    private String locallySpiedServiceText;
    private static String locallySpiedSingletonServiceText;
    private String locallySpiedInjectedServiceText;
    private String globallyInjectedSpyedGreetServiceText;

    @Spy
    private Service locallySpiedService = new ServiceImpl();

    @Spy
    @Singleton
    private Service locallySpiedSingletonService = new ServiceImpl();

    @Spy
    private Service locallySpiedInjectedService;

    @Context
    private GreetService globallyInjectedSpyedGreetService;

    @Override
    public void configureMocks(AbstractBinder binder) {
        // global service factory
        binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
        // global service spy factory !!! NOTE !!! proxy must be set to "false" 
        // otherwise Proxy will be injected instead of mock. This is how Jersey operates.
        binder.bindFactory(FactoryUtils.<GreetService>spy(ServiceFactory.class)).to(GreetService.class).in(RequestScoped.class).proxy(false);
    }

    @Before
    public void setUp() {
        locallySpiedServiceText = locallySpiedService.getText();
        locallySpiedSingletonServiceText = locallySpiedSingletonService.getText();
        locallySpiedInjectedServiceText = locallySpiedInjectedService.getText();
        globallyInjectedSpyedGreetServiceText = globallyInjectedSpyedGreetService.getText();
    }

    @Override
    public void afterServerStop() {
        assertSingletonSpy(locallySpiedSingletonService, locallySpiedSingletonServiceText);
    }

    private void assertSpy(Service service, String serviceGetText) {
        assertNotNull("Service should not be null", service);
        assertEquals("Service.getText() should return text", Service.RESPONSE_TEXT, serviceGetText);
        Mockito.verify(service, times(1)).getText();
    }

    private void assertSingletonSpy(Service service, String serviceGetText) {
        assertNotNull("Service should not be null", service);
        assertEquals("Service.getText() should return text", Service.RESPONSE_TEXT, serviceGetText);
        Mockito.verify(service, atLeast(5)).getText();
    }

    @Test
    public void locallySpiedServiceTest() {
        assertSpy(locallySpiedService, locallySpiedServiceText);
    }

    @Test
    @Request
    public void locallySpiedServiceWithExternalRequestTest() {
        assertSpy(locallySpiedService, locallySpiedServiceText);
    }

    @Test
    public void locallySpiedInjectedServiceTest() {
        assertSpy(locallySpiedInjectedService, locallySpiedInjectedServiceText);
    }

    @Test
    @Request
    public void locallySpiedInjectedServiceWithExternalRequestTest() {
        assertSpy(locallySpiedInjectedService, locallySpiedInjectedServiceText);
    }

    @Test
    public void globallyInjectedSpyedGreetServiceTest() {
        assertSpy(globallyInjectedSpyedGreetService, globallyInjectedSpyedGreetServiceText);
    }

    @Test
    @Request
    public void globallyInjectedSpyedGreetServiceWithExternalRequestTest() {
        assertSpy(globallyInjectedSpyedGreetService, globallyInjectedSpyedGreetServiceText);
    }

}
