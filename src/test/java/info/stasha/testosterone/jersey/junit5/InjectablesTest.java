package info.stasha.testosterone.jersey.junit5;


import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import org.junit.jupiter.api.Test;

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
