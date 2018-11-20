package info.stasha.testosterone.jersey.junit4.jersey.applicationlistener;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.times;

/**
 * Application event listener test<br>
 *
 * NOTE ApplicationEventListener is supported from Jersey version 2.1
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ApplicationListenerTest implements Testosterone {

    private static final ApplicationListener APPLICATION_LISTENER = Mockito.spy(new ApplicationListener());

    @Override
    public void configure(ResourceConfig config) {
        config.registerInstances(APPLICATION_LISTENER);
    }

    @Override
    public void afterServerStop() {
        // jersey version 2.1, 2.2, 2.3, 2.4 will invoke only 3 times other 4 times
        Mockito.verify(APPLICATION_LISTENER, atMost(4)).onEvent(Mockito.any(ApplicationEvent.class));

    }

    @Test
    public void applicationListenerTest() throws Exception {
        // jersey version 2.1, 2.2, 2.3, 2.4 will invoke only 2 times other 3 times
        Mockito.verify(APPLICATION_LISTENER, atMost(3)).onEvent(Mockito.any(ApplicationEvent.class));
        Mockito.verify(APPLICATION_LISTENER, times(1)).onRequest(Mockito.any(RequestEvent.class));

    }

}
