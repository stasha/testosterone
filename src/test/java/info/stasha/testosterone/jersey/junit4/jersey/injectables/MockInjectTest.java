package info.stasha.testosterone.jersey.junit4.jersey.injectables;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.FactoryUtils;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.jersey.junit4.jersey.service.MyService;
import info.stasha.testosterone.jersey.junit4.jersey.service.Service;
import info.stasha.testosterone.jersey.junit4.jersey.service.ServiceFactory;
import info.stasha.testosterone.jersey.junit4.jersey.service.ServiceImpl;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;

/**
 * Mock and Spy test.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class MockInjectTest implements Testosterone {

    private String locallyMockedServiceText;
    private static String locallyMockedSingletonServiceText;
    private String locallyMockedInjectedServiceText;
    private String globallyInjectedMockedMyServiceText;

    @Mock
    private Service locallyMockedService = new ServiceImpl();

    @Mock
    @Singleton
    private Service locallyMockedSingletonService = new ServiceImpl();

    @Mock
    private Service locallyMockedInjectedService;

    @Context
    private MyService globallyInjectedMockedMyService;
    
    @Override
    public void configureMocks(AbstractBinder binder) {
        // global service factory
        binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
        // global service mock factory !!! NOTE !!! proxy must be set to "false" 
        // otherwise Proxy will be injected instead of mock. This is how Jersey operates.
        binder.bindFactory(FactoryUtils.<MyService>mock(ServiceFactory.class)).to(MyService.class).in(RequestScoped.class).proxy(false);
    }

    @Before
    public void setUp() {
        locallyMockedServiceText = locallyMockedService.getText();
        locallyMockedSingletonServiceText = locallyMockedSingletonService.getText();
        locallyMockedInjectedServiceText = locallyMockedInjectedService.getText();
        globallyInjectedMockedMyServiceText = globallyInjectedMockedMyService.getText();
        
    }

    @Override
    public void afterServerStop() {
        assertSingletonMock(locallyMockedSingletonService, locallyMockedSingletonServiceText);
    }

    private void assertMock(Service service, String serviceGetText) {
        assertNotNull("Service should not be null", service);
        assertNull("Service.getText() should be null", serviceGetText);
        Mockito.verify(service, times(1)).getText();
    }

    private void assertSingletonMock(Service service, String serviceGetText) {
        assertNotNull("Service should not be null", service);
        assertNull("Service.getText() should be null", serviceGetText);
        Mockito.verify(service, atLeast(5)).getText();
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
}
