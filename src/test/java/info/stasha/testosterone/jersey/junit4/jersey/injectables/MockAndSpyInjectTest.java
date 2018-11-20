package info.stasha.testosterone.jersey.junit4.jersey.injectables;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.FactoryUtils;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.jersey.junit4.jersey.service.GreetService;
import info.stasha.testosterone.jersey.junit4.jersey.service.MyService;
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
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
public class MockAndSpyInjectTest implements Testosterone {

    private String locallyMockedServiceText;
    private static String locallyMockedSingletonServiceText;
    private String locallyMockedInjectedServiceText;
    private String globallyInjectedMockedMyServiceText;
    private String locallySpiedServiceText;
    private static String locallySpiedSingletonServiceText;
    private String locallySpiedInjectedServiceText;
    private String globallyInjectedSpyedGreetServiceText;

    @Mock
    private Service locallyMockedService = new ServiceImpl();

    @Mock
    @Singleton
    private Service locallyMockedSingletonService = new ServiceImpl();

    @Mock
    private Service locallyMockedInjectedService;

    @Context
    private MyService globallyInjectedMockedMyService;

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
        // global service mock factory !!! NOTE !!! proxy must be set to "false" 
        // otherwise Proxy will be injected instead of mock. This is how Jersey operates.
        binder.bindFactory(FactoryUtils.<MyService>mock(ServiceFactory.class)).to(MyService.class).in(RequestScoped.class).proxy(false);
        // global service spy factory !!! NOTE !!! proxy must be set to "false" 
        // otherwise Proxy will be injected instead of mock. This is how Jersey operates.
        binder.bindFactory(FactoryUtils.<GreetService>spy(ServiceFactory.class)).to(GreetService.class).in(RequestScoped.class).proxy(false);
    }

    @Before
    public void setUp() {
        locallyMockedServiceText = locallyMockedService.getText();
        locallyMockedSingletonServiceText = locallyMockedSingletonService.getText();
        locallyMockedInjectedServiceText = locallyMockedInjectedService.getText();
        globallyInjectedMockedMyServiceText = globallyInjectedMockedMyService.getText();
        locallySpiedServiceText = locallySpiedService.getText();
        locallySpiedSingletonServiceText = locallySpiedSingletonService.getText();
        locallySpiedInjectedServiceText = locallySpiedInjectedService.getText();
        globallyInjectedSpyedGreetServiceText = globallyInjectedSpyedGreetService.getText();
    }

    @Override
    public void afterServerStop() {
        assertSingletonMock(locallyMockedSingletonService, locallyMockedSingletonServiceText);
        assertSingletonSpy(locallySpiedSingletonService, locallySpiedSingletonServiceText);
    }

    private void assertMock(Service service, String serviceGetText) {
        assertNotNull("Service should not be null", service);
        assertNull("Service.getText() should be null", serviceGetText);
        Mockito.verify(service, times(1)).getText();
    }

    private void assertSingletonMock(Service service, String serviceGetText) {
        assertNotNull("Service should not be null", service);
        assertNull("Service.getText() should be null", serviceGetText);
        Mockito.verify(service, atLeast(15)).getText();
    }

    private void assertSpy(Service service, String serviceGetText) {
        assertNotNull("Service should not be null", service);
        assertEquals("Service.getText() should return text", Service.RESPONSE_TEXT, serviceGetText);
        Mockito.verify(service, times(1)).getText();
    }

    private void assertSingletonSpy(Service service, String serviceGetText) {
        assertNotNull("Service should not be null", service);
        assertEquals("Service.getText() should return text", Service.RESPONSE_TEXT, serviceGetText);
        Mockito.verify(service, atLeast(15)).getText();
    }

    @Test
    public void locallyMockedServiceTest() {
        assertMock(locallyMockedService, locallyMockedServiceText);
    }

    @Test
    @Request
    public void locallyMockedServiceWithExternalRequestTest() {
        assertMock(locallyMockedService, locallyMockedServiceText);
    }

    @Test
    public void locallyMockedInjectedServiceTest() {
        assertMock(locallyMockedInjectedService, locallyMockedInjectedServiceText);
    }

    @Test
    @Request
    public void locallyMockedInjectedServiceWithExternalRequestTest() {
        assertMock(locallyMockedInjectedService, locallyMockedInjectedServiceText);
    }

    @Test
    public void globallyInjectedMockedMyServiceTest() {
        assertMock(globallyInjectedMockedMyService, globallyInjectedMockedMyServiceText);
    }

    @Test
    @Request
    public void globallyInjectedMockedMyServiceWithExternalRequestTest() {
        assertMock(globallyInjectedMockedMyService, globallyInjectedMockedMyServiceText);
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
