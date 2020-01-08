package info.stasha.testosterone.jersey.junit5.helidon;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.junit4.helidon.ApplicationScopedCdiTestService;
import info.stasha.testosterone.jersey.junit4.helidon.DependentCdiTestService;
import info.stasha.testosterone.jersey.junit4.helidon.RequestScopedCdiTestService;
import info.stasha.testosterone.jersey.junit5.Testosterone;
import info.stasha.testosterone.servers.HelidonMpServerConfig;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.Config;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author stasha
 */
@RequestScoped
@Configuration(serverConfig = HelidonMpServerConfig.class)
public class HelidonJunit5Test implements Testosterone {

    // cdi injection
    @Inject
    private RequestScopedCdiTestService weldClass;

    // cdi injection
    @Inject
    private DependentCdiTestService testClass;

    @Inject
    private Config config;

    // jersey injection
    @Context
    UriInfo uriInfo;

    @Test
    public void test() {
        assertEquals(weldClass.getDependentMessage(),
                DependentCdiTestService.MESSAGE, "Returned string from weld class should equal");
        assertEquals(weldClass.getApplicationScopedMessage(),
                ApplicationScopedCdiTestService.MESSAGE, "Returned string from weld class should equal");
        assertEquals(testClass.getMessage(), DependentCdiTestService.MESSAGE, "Returned string from weld class should equal");
        assertNotNull(config, "UriInfo should not be null");
        assertNotNull(uriInfo, "UriInfo should not be null");
    }
}
