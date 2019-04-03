package info.stasha.testosterone.jersey.junit4.jersey.runtimebinding;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.core.Context;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class RuntimeBindingNonExistinPackageTest implements Testosterone {

    @Override
    public void configure(ResourceConfig config) {
        config.packages("some.non.existing.package");
    }

    @Context
    private String stringFromFactory;

    @Test
    public void runtimeBindedFactoryTest() {
        assertNull("Runtime binded factory should not be binded so string should be null.", stringFromFactory);
    }

}
