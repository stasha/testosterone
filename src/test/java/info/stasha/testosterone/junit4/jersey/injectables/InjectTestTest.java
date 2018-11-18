package info.stasha.testosterone.junit4.jersey.injectables;

import info.stasha.testosterone.annotation.InjectTest;
import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.junit4.jersey.service.ServiceTest;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@Integration(
        ServiceTest.class
)
@RunWith(TestosteroneRunner.class)
public class InjectTestTest implements Testosterone {

    @InjectTest
    ServiceTest serviceTest;

    @Test
    public void Test() {
        assertNotNull("Test should be injected", serviceTest);
    }

}
