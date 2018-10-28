package info.stasha.testosterone.servlet;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit.TestosteroneRunner;
import info.stasha.testosterone.jersey.resource.Resource;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Servlet test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ServletTest implements Testosterone {

	AccessServlet servlet = Mockito.spy(new AccessServlet());

	@Override
	public void configure(ServletContainerConfig sc) {
		sc.setJerseyServletPath("/jersey").addServlet(servlet, "/*");
	}

	@Test
	@Request(url = Resource.HELLO_WORLD_PATH)
	public void test(Response resp) throws ServletException, IOException {
		Mockito.verify(servlet, times(1)).service(any(HttpServletRequest.class), any(HttpServletResponse.class));
	}

}
