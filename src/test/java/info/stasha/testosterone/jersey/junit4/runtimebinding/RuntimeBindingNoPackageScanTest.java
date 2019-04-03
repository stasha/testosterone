package info.stasha.testosterone.jersey.junit4.runtimebinding;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.core.Context;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class RuntimeBindingNoPackageScanTest implements Testosterone {

    @Context
    private String stringFromFactory;

    @Test
    public void runtimeBindedFactoryTest() {
        assertNull("Factory should not be binded because there is no package scan.", stringFromFactory);
    }

}
