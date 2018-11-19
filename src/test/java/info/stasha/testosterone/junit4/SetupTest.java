package info.stasha.testosterone.junit4;

import info.stasha.testosterone.Setup;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import info.stasha.testosterone.jersey.Testosterone;
import org.glassfish.hk2.api.ServiceLocator;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 *
 * @author stasha
 */
public class SetupTest {

    Testosterone test = Mockito.mock(PlaygroundTest.class);
    TestConfig config = new JerseyTestConfig(test);
    ServiceLocator locator = Mockito.mock(ServiceLocator.class);

    Setup setup = new Setup(config);
    Setup setup2 = new Setup(config);

    @Test
    public void beforeServerStartTest() throws Exception {
        setup.beforeServerStart(test);
        setup.beforeServerStart(test);
        Mockito.verify(test, times(1)).beforeServerStart();
        setup.toString();
    }

    @Test
    public void afterServerStartTest() throws Exception {
        setup.afterServerStart(test);
        setup.afterServerStart(test);
        Mockito.verify(test, times(1)).afterServerStart();
    }

    @Test
    public void beforeServerStopTest() throws Exception {
        setup.beforeServerStop(test);
        setup.beforeServerStop(test);
        Mockito.verify(test, times(1)).beforeServerStop();
    }

    @Test
    public void afterServerStopTest() throws Exception {
        setup.afterServerStop(test);
        setup.afterServerStop(test);
        Mockito.verify(test, times(1)).afterServerStop();
    }
}
