package info.stasha.testosterone.jersey.junit4.jersey.runtimebinding;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.core.Context;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class RuntimeBindingWithPackageScenTest implements Testosterone {

    @Override
    public void configure(ResourceConfig config) {
        config.packages("info.stasha.testosterone.jersey.junit4");
    }

    @Context
    private String stringFromFactory;

    @Test
    public void runtimeBindedFactoryTest() {
        assertEquals("String from runtime binded factory should equal",
                RuntimeBindingListener.STRING_FROM_RUNTIME_BINDED_FACTORY, stringFromFactory);
    }

}
