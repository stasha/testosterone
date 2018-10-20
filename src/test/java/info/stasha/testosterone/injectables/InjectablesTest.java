package info.stasha.testosterone.injectables;

import info.stasha.testosterone.JerseyRequestTest;
import info.stasha.testosterone.JerseyRequestTestRunner;
import info.stasha.testosterone.RequestTest;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;
import org.junit.runner.RunWith;

/**
 * Test injectables
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class InjectablesTest extends JerseyRequestTest {

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

	@RequestTest
	public void injectablesTest() {
		assertNotNull("Application should not be null", application);
		assertNotNull("HttpHeaders should not be null", httpHeaders);
		assertNotNull("Request should not be null", request);
		assertNotNull("SecurityContext should not be null", securityContext);
		assertNotNull("UriInfo should not be null", uriInfo);
		assertNotNull("Configuration should not be null", conf);
		assertNotNull("ResourceContext should not be null", resourceContext);
		assertNotNull("Providers should not be null", providers);
	}

	@RequestTest
	public void methodInjectablesTest(
			@Context Application application,
			@Context HttpHeaders httpHeaders,
			@Context Request request,
			@Context SecurityContext securityContext,
			@Context UriInfo uriInfo,
			@Context Configuration conf,
			@Context ResourceContext resourceContext,
			@Context Providers providers
	) {
		assertNotNull("Application should not be null", application);
		assertNotNull("HttpHeaders should not be null", httpHeaders);
		assertNotNull("Request should not be null", request);
		assertNotNull("SecurityContext should not be null", securityContext);
		assertNotNull("UriInfo should not be null", uriInfo);
		assertNotNull("Configuration should not be null", conf);
		assertNotNull("ResourceContext should not be null", resourceContext);
		assertNotNull("Providers should not be null", providers);
	}

}
