package info.stasha.testosterone.injectables;

import info.stasha.testosterone.Testosterone;
import info.stasha.testosterone.jersey.JerseyRequestTestRunner;
import info.stasha.testosterone.jersey.JerseyWebRequestTest;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;
import static org.junit.Assert.assertNotNull;
import org.junit.runner.RunWith;

/**
 * Test injectables 
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class InjectablesTest extends JerseyWebRequestTest {

	@Context
	private Application application;

	@Context
	private HttpHeaders httpHeaders;

	@Context
	private Request request;

	@Context
	private SecurityContext securityContext;

	@Context
	private UriInfo uriInfo;

	@Context
	private Configuration conf;

	@Context
	private ResourceContext resourceContext;

	@Context
	private Providers providers;

	@Context
	private HttpServletRequest httpServletRequest;

	@Context
	private HttpServletResponse httpServletResponse;

	@Context
	private ServletConfig servletConfig;

	@Context
	private ServletContext servletContext;

	/**
	 * Jersey version 2.0, 2.1, 2.2, 2.3 will fail with message "Not inside a
	 * request scope."
	 */
	@Test
	public void injectablesTest() {
		assertNotNull("Application should not be null", application);
		assertNotNull("HttpHeaders should not be null", httpHeaders);
		assertNotNull("Request should not be null", request);
		assertNotNull("SecurityContext should not be null", securityContext);
		assertNotNull("UriInfo should not be null", uriInfo);
		assertNotNull("Configuration should not be null", conf);
		assertNotNull("ResourceContext should not be null", resourceContext);
		assertNotNull("Providers should not be null", providers);
		assertNotNull("HttpServletRequest should not be null", httpServletRequest);
		assertNotNull("HttpServletResponse should not be null", httpServletResponse);
		assertNotNull("ServletConfig should not be null", servletConfig);
		assertNotNull("ServletContext should not be null", servletContext);
	}

	/**
	 * Jersey version 2.0 will fail with message "Not inside a request scope."
	 *
	 * @param application
	 * @param httpHeaders
	 * @param request
	 * @param securityContext
	 * @param uriInfo
	 * @param conf
	 * @param resourceContext
	 * @param providers
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param servletConfig
	 * @param servletContext
	 */
	@Test
	public void methodInjectablesTest(
			@Context Application application,
			@Context HttpHeaders httpHeaders,
			@Context Request request,
			@Context SecurityContext securityContext,
			@Context UriInfo uriInfo,
			@Context Configuration conf,
			@Context ResourceContext resourceContext,
			@Context Providers providers,
			@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse,
			@Context ServletConfig servletConfig,
			@Context ServletContext servletContext
	) {
		assertNotNull("Application should not be null", application);
		assertNotNull("HttpHeaders should not be null", httpHeaders);
		assertNotNull("Request should not be null", request);
		assertNotNull("SecurityContext should not be null", securityContext);
		assertNotNull("UriInfo should not be null", uriInfo);
		assertNotNull("Configuration should not be null", conf);
		assertNotNull("ResourceContext should not be null", resourceContext);
		assertNotNull("Providers should not be null", providers);
		assertNotNull("HttpServletRequest should not be null", httpServletRequest);
		assertNotNull("HttpServletResponse should not be null", httpServletResponse);
		assertNotNull("ServletConfig should not be null", servletConfig);
		assertNotNull("ServletContext should not be null", servletContext);
	}

}
