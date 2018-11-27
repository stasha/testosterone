package info.stasha.testosterone.jersey.testng;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Test injectables 
 *
 * @author stasha
 */
public class InjectablesTest implements Testosterone {

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

	@Test
	public void injectablesTest() {
		System.out.println("Inside injectableTest");
		assertNotNull(application, "Application should not be null");
		assertNotNull(httpHeaders, "HttpHeaders should not be null");
		assertNotNull(request, "Request should not be null");
		assertNotNull(securityContext, "SecurityContext should not be null");
		assertNotNull(uriInfo, "UriInfo should not be null");
		assertNotNull(conf, "Configuration should not be null");
		assertNotNull(resourceContext, "ResourceContext should not be null");
		assertNotNull(providers, "Providers should not be null");
		assertNotNull(httpServletRequest, "HttpServletRequest should not be null");
		assertNotNull(httpServletResponse, "HttpServletResponse should not be null");
		assertNotNull(servletConfig, "ServletConfig should not be null");
		assertNotNull(servletContext, "ServletContext should not be null");
	}

	@Test
    @Parameters({"*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*"}) // required by TestNG (* can be replaced by anything)
	public void methodInjectablesTest(
            /* @Optional annotation is required by TestNG when using method arguments */
			@Optional @Context Application application,
			@Optional @Context HttpHeaders httpHeaders,
			@Optional @Context Request request,
			@Optional @Context SecurityContext securityContext,
			@Optional @Context UriInfo uriInfo,
			@Optional @Context Configuration conf,
			@Optional @Context ResourceContext resourceContext,
			@Optional @Context Providers providers,
			@Optional @Context HttpServletRequest httpServletRequest,
			@Optional @Context HttpServletResponse httpServletResponse,
			@Optional @Context ServletConfig servletConfig,
			@Optional @Context ServletContext servletContext
	) {
		System.out.println("Inside injectableMethodTest");
		assertNotNull(application, "Application should not be null");
		assertNotNull(httpHeaders, "HttpHeaders should not be null");
		assertNotNull(request, "Request should not be null");
		assertNotNull(securityContext, "SecurityContext should not be null");
		assertNotNull(uriInfo, "UriInfo should not be null");
		assertNotNull(conf, "Configuration should not be null");
		assertNotNull(resourceContext, "ResourceContext should not be null");
		assertNotNull(providers, "Providers should not be null");
		assertNotNull(httpServletRequest, "HttpServletRequest should not be null");
		assertNotNull(httpServletResponse, "HttpServletResponse should not be null");
		assertNotNull(servletConfig, "ServletConfig should not be null");
		assertNotNull(servletContext, "ServletContext should not be null");
	}

}
