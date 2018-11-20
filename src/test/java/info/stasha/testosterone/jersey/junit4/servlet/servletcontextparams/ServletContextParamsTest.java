package info.stasha.testosterone.jersey.junit4.servlet.servletcontextparams;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Servlet context params test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ServletContextParamsTest implements Testosterone, ServletContextListener {

	private static final String TEST_PARAM = "test.param";
	private static final String TEST_PARAM_VALUE = TEST_PARAM + ".value";

	private static final String TEST_PARAM_1 = TEST_PARAM + 1;
	private static final String TEST_PARAM_VALUE_1 = TEST_PARAM_VALUE + 1;
	private static final String TEST_PARAM_2 = TEST_PARAM + 2;
	private static final String TEST_PARAM_VALUE_2 = TEST_PARAM_VALUE + 2;

	@Override
	public void configure(ServletContainerConfig sc) {
		sc.addListener(this)
				.addContextParam(TEST_PARAM_1, TEST_PARAM_VALUE_1)
				.addContextParam(TEST_PARAM_2, TEST_PARAM_VALUE_2);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		assertEquals("Context param should equal", TEST_PARAM_VALUE_1,
				sce.getServletContext().getInitParameter(TEST_PARAM_1));
		assertEquals("Context param should equal", TEST_PARAM_VALUE_2,
				sce.getServletContext().getInitParameter(TEST_PARAM_2));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("destroyed");
	}

	@Test
	@Request(url = "/", expectedStatus = 404)
	public void test(Response resp) throws ServletException, IOException {
		assertEquals("Status should be 404", 404, resp.getStatus());
	}

}
