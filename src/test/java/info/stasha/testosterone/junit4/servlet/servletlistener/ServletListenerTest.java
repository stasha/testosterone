package info.stasha.testosterone.junit4.servlet.servletlistener;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Servlet event listener test
 *
 * TODO: test all methods in ServletListener
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ServletListenerTest implements Testosterone {

    private ServletListener listener = Mockito.spy(new ServletListener());
    
    @Override
    public void configure(ServletContainerConfig sc) {
        sc.addListener(listener);
    }

    @Override
    public void afterServerStop() {
        Mockito.verify(listener, times(1)).contextDestroyed(any(ServletContextEvent.class));

        // There will be two requests destroyed, one that initialized the test 
        // and other performed by @Request annotation
        Mockito.verify(listener, times(2)).requestDestroyed(any(ServletRequestEvent.class));
    }

    @Test
    @Request(url = "/", expectedStatus = 404)
    public void test(Response resp) throws ServletException, IOException {
        Mockito.verify(listener, times(1)).contextInitialized(any(ServletContextEvent.class));

        // There will be two requests initialized, one to initialize the test 
        // and other by @Request annotation
        Mockito.verify(listener, times(2)).requestInitialized(any(ServletRequestEvent.class));
    }

}
