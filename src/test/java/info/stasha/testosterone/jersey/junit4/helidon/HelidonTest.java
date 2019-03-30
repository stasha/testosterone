package info.stasha.testosterone.jersey.junit4.helidon;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.cdi.CdiConfig;
import info.stasha.testosterone.cdi.weld.WeldUtils;
import info.stasha.testosterone.jersey.junit4.helidon.ClassProducer.ProducedClass;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servers.HelidonMpServerConfig;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.testng.AssertJUnit.assertNull;

/**
 *
 * @author stasha
 */
@RequestScoped
@RunWith(TestosteroneRunner.class)
@Configuration(serverConfig = HelidonMpServerConfig.class)
public class HelidonTest implements Testosterone {

    @Override
    public void configureMocks(CdiConfig config) {
        config.spy(DependentCdiTestServiceAlternative.class).setScope(RequestScoped.class);
        config.spy(ApplicationScopedCdiTestService.class).setScope(ApplicationScoped.class);
        config.spy(ClassProducer.class).setScope(ApplicationScoped.class);
        config.mock(SimpleMessageClass.class);
    }

    @Override
    public void afterServerStop() throws Exception {
        Mockito.verify(testMock).destroy();

        // doesn't work at the moment
//        Mockito.verify(longProducerMock).dispose(any());
    }

    private static DependentCdiTestService testMock;
    private static ClassProducer longProducerMock;

    // cdi injection
    @Inject
    private RequestScopedCdiTestService weldClass;

    // cdi injection
    @Inject
    private DependentCdiTestService testClass;

    @Inject
    private ApplicationScopedCdiTestService appScopedService;

    @Inject
    private Config config;

    @Inject
    private ClassProducer classProducer;

    @Inject
    private ProducedClass producedClass;

    @Inject
    private SimpleMessageClass simpleInterface;

    @Inject
    @ConfigProperty(name = "currency", defaultValue = "eur")
    private String currency;

    @Inject
    @ConfigProperty(name = "maxsize", defaultValue = "10")
    private Integer maxSize;

    // jersey injection
    @Context
    UriInfo uriInfo;

    @Test
    public void test() {
        assertEquals("Returned string from weld class should equal",
                DependentCdiTestServiceAlternative.MESSAGE, weldClass.getDependentMessage());
        assertEquals("Returned string from weld class should equal",
                ApplicationScopedCdiTestService.MESSAGE, weldClass.getApplicationScopedMessage());
        assertNotNull("Returned uriInfo from weld class should not be null", weldClass.getUriInfo());
        assertEquals("Returned string from weld class should equal", DependentCdiTestServiceAlternative.MESSAGE, testClass.getMessage());
        assertNotNull("UriInfo should not be null", config);
        assertEquals("Application currency should equal", "SESTRICIE", currency);
        assertEquals("Application minsize should equal", 3, (int) maxSize);
        assertNotNull("UriInfo should not be null", uriInfo);
        assertNotNull("Time should not be null", producedClass);
        assertNull("Call on mock object should return null", simpleInterface.getMessage());

        testMock = WeldUtils.unwrap(testClass);
        longProducerMock = WeldUtils.unwrap(classProducer);

        Mockito.verify(WeldUtils.unwrap(appScopedService), times(1)).getMessage();
        Mockito.verify(WeldUtils.unwrap(simpleInterface), times(1)).getMessage();

        // because it's dependent scoped there will be just 1 times init
        Mockito.verify(testMock).init();
        Mockito.verify(testMock, times(2)).getMessage();
    }
}
