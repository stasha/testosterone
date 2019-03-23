package info.stasha.testosterone.jersey.junit4.helidon;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.helidon.ApplicationScopedCdiTestService;
import info.stasha.testosterone.helidon.DependentCdiTestService;
import info.stasha.testosterone.helidon.RequestScopedCdiTestService;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servers.HelidonMpServerConfig;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RequestScoped
@RunWith(TestosteroneRunner.class)
@Configuration(serverConfig = HelidonMpServerConfig.class)
public class HelidonTest implements Testosterone {

    // cdi injection
    @Inject
    private RequestScopedCdiTestService weldClass;

    // cdi injection
    @Inject
    private DependentCdiTestService testClass;

    @Inject
    private Config config;

    @Inject
    @ConfigProperty(name = "currency", defaultValue = "eur")
    private String currency;

    @Inject
    @ConfigProperty(name = "maxsize", defaultValue = "10")
    private Integer maxSize;

    // jersey injection
    @Context
    UriInfo uriInfo;

    @GET
    public String getTest() {
        System.out.println(testClass.getMessage());
        return weldClass.getDependentMessage();
    }

    @Test
    public void test() {
        assertEquals("Returned string from weld class should equal", 
                DependentCdiTestService.MESSAGE, weldClass.getDependentMessage());
        assertEquals("Returned string from weld class should equal", 
                ApplicationScopedCdiTestService.MESSAGE, weldClass.getApplicationScopedMessage());
        assertNotNull("Returned uriInfo from weld class should not be null", weldClass.getUriInfo());
        assertEquals("Returned string from weld class should equal", DependentCdiTestService.MESSAGE, testClass.getMessage());
        assertNotNull("UriInfo should not be null", config);
        assertEquals("Application currency should equal", "SESTRICIE", currency);
        assertEquals("Application minsize should equal", 3, (int) maxSize);
        assertNotNull("UriInfo should not be null", uriInfo);
    }
}
