package info.stasha.testosterone.jersey.junit4.runtimebinding;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class RuntimeBindingTest implements Testosterone {

    @Override
    public void configure(ResourceConfig config) {
        config.packages("info");
    }

    @Context
    private ServiceLocator locator;

    @Test
    public void runtimeBindedFactoryTest() {
        assertEquals("String from runtime binded factory should equal", 
                RuntimeBindingFeature.STRING_FROM_RUNTIME_BINDED_FACTORY, locator.getService(String.class));
    }

}
