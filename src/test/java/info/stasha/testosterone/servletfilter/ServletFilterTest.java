package info.stasha.testosterone.servletfilter;

import info.stasha.testosterone.servlet.*;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit.TestosteroneRunner;
import info.stasha.testosterone.resource.Resource;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Servlet filter test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ServletFilterTest implements Testosterone {

	AccessFilter filter = Mockito.spy(new AccessFilter());

	@Override
	public void configure(ServletContainerConfig sc) {
		sc.addFilter(filter, "/*");
	}

	@After
	public void tearDown() {
		Mockito.verify(filter, times(1)).destroy();
	}

	@Test
	@Request(url = Resource.HELLO_WORLD_PATH)
	public void test(Response resp) throws ServletException, IOException {
		Mockito.verify(filter, times(1)).init(any(FilterConfig.class));
		Mockito.verify(filter, times(1)).doFilter(
				any(ServletRequest.class),
				any(ServletResponse.class),
				any(FilterChain.class));
	}

}
