package info.stasha.testosterone.servlet.servletlistener;

import info.stasha.testosterone.servlet.*;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit.TestosteroneRunner;
import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;
import javax.ws.rs.core.Response;
import org.junit.After;
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

	ServletListener listener = Mockito.spy(new ServletListener());

	@Override
	public void configure(ServletContainerConfig sc) {
		sc.addListener(listener);
	}

	@After
	public void tearDown() {
		Mockito.verify(listener, times(1)).contextDestroyed(any(ServletContextEvent.class));
	}

	@Test
	@Request(url = "/")
	public void test(Response resp) throws ServletException, IOException {
		Mockito.verify(listener, times(1)).contextInitialized(any(ServletContextEvent.class));
		Mockito.verify(listener, times(1)).requestInitialized(any(ServletRequestEvent.class));
		Mockito.verify(listener, times(1)).requestDestroyed(any(ServletRequestEvent.class));
	}

}
