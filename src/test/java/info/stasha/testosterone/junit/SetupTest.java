package info.stasha.testosterone.junit;

import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.Setup;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.PlaygroundTest;
import java.io.IOException;
import java.lang.reflect.Field;
import org.glassfish.hk2.api.ServiceLocator;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 *
 * @author stasha
 */
public class SetupTest {

    Testosterone test = Mockito.mock(PlaygroundTest.class);
    ServerConfig config = Mockito.mock(ServerConfig.class);
    ServiceLocator locator = Mockito.mock(ServiceLocator.class);

    Setup setup = new Setup(test, config);
    Setup setup2 = new Setup(test, config);

    @Test(expected = IllegalArgumentException.class)
    public void newInstanceTest() {
        setup = new Setup(null, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newInstanceTest2() {
        setup = new Setup(test, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newInstanceTest3() {
        setup = new Setup(null, null);
    }

    @Test
    public void setRootTest() {
        setup.setRoot(setup);
        setup.setRoot(setup2);
        assertEquals("Setups should equal", setup, setup.getRoot());
    }

    @Test
    public void filterTest() throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        Field l = setup.getClass().getDeclaredField("locator");
        l.setAccessible(true);
        Field t = setup.getClass().getDeclaredField("testosterone");
        t.setAccessible(true);

        l.set(setup, locator);
        setup.filter(null, null);
        Mockito.verify(locator).inject(any());
        
//        l.set(setup, null);
        t.set(setup, null);
        setup.filter(null, null);
        Mockito.verify(locator).inject(any());
    }

    @Test
    public void beforeServerStartTest() throws Exception {
        setup.beforeServerStart(test);
        setup.beforeServerStart(test);
        Mockito.verify(test, times(1)).beforeServerStart();
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
