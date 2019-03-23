//package info.stasha.testosterone.jersey.junit5.helidon;
//
//import info.stasha.testosterone.annotation.Configuration;
//import info.stasha.testosterone.helidon.ApplicationScopedCdiTestService;
//import info.stasha.testosterone.helidon.DependentCdiTestService;
//import info.stasha.testosterone.helidon.RequestScopedCdiTestService;
//import info.stasha.testosterone.jersey.junit5.Testosterone;
//import info.stasha.testosterone.servers.HelidonMpServerConfig;
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.UriInfo;
//import org.eclipse.microprofile.config.Config;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import org.junit.jupiter.api.Test;
//
///**
// *
// * @author stasha
// */
//@RequestScoped
//@Configuration(serverConfig = HelidonMpServerConfig.class)
//public class HelidonTest implements Testosterone {
//
////     weld is complaining if there are more tests with "same code" so
////     only junit4 test is enabled by default (I'm to lazy to investigate).
//
//    // cdi injection
//    @Inject
//    private RequestScopedCdiTestService weldClass;
//
//    // cdi injection
//    @Inject
//    private DependentCdiTestService testClass;
//
//    @Inject
//    private Config config;
//
//    @Inject
//    @ConfigProperty(name = "currency", defaultValue = "eur")
//    private String currency;
//
//    @Inject
//    @ConfigProperty(name = "maxsize", defaultValue = "10")
//    private Integer maxSize;
//
//    // jersey injection
//    @Context
//    UriInfo uriInfo;
//
//
//    @Test
//    public void test() {
//        assertEquals(weldClass.getDependentMessage(), 
//                DependentCdiTestService.MESSAGE, "Returned string from weld class should equal");
//        assertEquals(weldClass.getApplicationScopedMessage(), 
//                ApplicationScopedCdiTestService.MESSAGE, "Returned string from weld class should equal");
//        assertEquals(testClass.getMessage(), DependentCdiTestService.MESSAGE, "Returned string from weld class should equal");
//        assertNotNull(config, "UriInfo should not be null");
//        assertEquals(currency, "SESTRICIE", "Application currency should equal");
//        assertEquals((int) maxSize, 3, "Application minsize should equal");
//        assertNotNull(uriInfo, "UriInfo should not be null");
//    }
//}
