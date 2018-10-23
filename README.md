# testosterone
Testosterone enables Jersey usage directly in JUnit test classes. 
In other words, test classes become Jersey resource classes with full JUnit functionality support. 
Components can be injected using @Context or @Inject which is ideal for testing single component functionality. 

[![Build Status](https://travis-ci.org/stasha/testosterone.svg?branch=master)](https://travis-ci.org/stasha/testosterone)
[![CircleCI](https://circleci.com/gh/stasha/testosterone/tree/master.svg?style=svg)](https://circleci.com/gh/stasha/testosterone/tree/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/info.stasha/testosterone/badge.svg)](https://maven-badges.herokuapp.com/maven-central/info.stasha/testosterone)

### Install using maven:
```
<dependency>
  <groupId>info.stasha</groupId>
  <artifactId>testosterone</artifactId>
  <version>${testosterone.version}</version>
</dependency>
```
### Downlod from: 
```
https://repo.maven.apache.org/maven2/info/stasha/testosterone/
```

# Examples

### Application event listener test
```
@RunWith(JerseyRequestTestRunner.class)
public class ApplicationListenerTest extends JerseyRequestTest {

	private static final ApplicationListener APPLICATION_LISTENER = Mockito.spy(new ApplicationListener());

	public ApplicationListenerTest() {
		this.configuration.registerInstances(APPLICATION_LISTENER);
	}

	@AfterClass
	public static void afterClass() {
		Mockito.verify(APPLICATION_LISTENER, times(4)).onEvent(Mockito.any(ApplicationEvent.class));
	}

	@Test
	public void applicationListenerTest() {
		Mockito.verify(APPLICATION_LISTENER, times(3)).onEvent(Mockito.any(ApplicationEvent.class));
		Mockito.verify(APPLICATION_LISTENER, times(1)).onRequest(Mockito.any(RequestEvent.class));
	}

}
```
### BeanParam test
```
@RunWith(JerseyRequestTestRunner.class)
public class BeanParamTest extends JerseyRequestTest {

	private static final String FIRST_NAME = "Jon";
	private static final String LAST_NAME = "Doe";
	private static final String STATE = "sleeping";

	@GET
	@Path("beanParam/{state}")
	public void beanParams(@BeanParam User user) {
		assertEquals("First Name should equal", FIRST_NAME, user.getFirstName());
		assertEquals("Last Name should equal", LAST_NAME, user.getLastName());
		assertEquals("State should equal", STATE, user.getState());
	}

	@Test
	public void testParams() {
		Response resp = target().path("beanParam").path(STATE)
				.queryParam("firstName", FIRST_NAME)
				.queryParam("lastName", LAST_NAME)
				.request().get();

		assertEquals("Status code should be 204", 204, resp.getStatus());
	}

}
```

### Exception mapper test
```
@RunWith(JerseyRequestTestRunner.class)
public class ExceptionMappingProviderTest extends JerseyRequestTest {

	public static final String PATH = "exceptionMapping";

	public ExceptionMappingProviderTest() {
		this.configuration.register(ExceptionMapperProvider.class);
	}

	@GET
	@Path(PATH)
	@DontIntercept
	public String post() {
		throw new IllegalStateException();
	}

	@Test
	public void testmethod() {
		String resp = target().path(PATH).request().get().readEntity(String.class);
		assertEquals("ExceptionMappingProvider should return message", ExceptionMapperProvider.MESSAGE, resp);
	}

}
```
### Expected exception test
```
@RunWith(JerseyRequestTestRunner.class)
public class ExpectedExceptionTest extends JerseyRequestTest {

	public ExpectedExceptionTest() {
		this.abstractBinder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@Test(expected = IllegalStateException.class)
	public void illegalStateExceptionTest(@Context Service service) {
		service.throwIllegalStateException();
	}

	@GET
	@Path("exceptionTest")
	public String throwIllegalStateException(@Context Service service) {
		service.throwIllegalStateException();
		return "not reachable";
	}

	@Test(expected = IllegalStateException.class)
	public void illegalStateExceptionByRequestTest() {
		target().path("exceptionTest").request().get();
	}

}
```

### Injectables test
```
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
```

### ReadWrite interceptor test
```
@RunWith(JerseyRequestTestRunner.class)
public class ReadWriteInterceptorTest extends JerseyRequestTest {

	public static final String PATH = "changeTextInterceptor";

	public ReadWriteInterceptorTest() {
		this.configuration.registerInstances(new ReadWriteInterceptor());
	}

	@GET
	@Path(PATH)
	public String post() {
		return "success";
	}

	@Test
	public void testmethod(String data) {
		// Interceptor should pass string data
		assertEquals("Interceptor should pass string data", ReadWriteInterceptor.READ_FROM_TEXT, data);

		// without interceptor, returned text should be "success"
		String resp = target().path(PATH).request().get().readEntity(String.class);

		// Text should be changed by interceptor
		assertEquals("Interceptor should return string data", ReadWriteInterceptor.WRITE_TO_TEXT, resp);
	}

}
```


### Request test
```
@RunWith(JerseyRequestTestRunner.class)
public class RequestTest extends JerseyRequestTest {

	public RequestTest() {
		this.configuration.register(Resource.class);
		this.abstractBinder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	/**
	 * Multi request test.
	 *
	 * @param service
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	@Test
	@GET
	@Path("test/{service}/{id}")
	@Requests(
			repeat = 2,
			requests = {
				@Request(url = "test/[a-z]{10,20}/[0-9]{1,10}?firstName=Jon&lastName=Doe"),
				@Request(url = "test2/car/[a-z]{10,20}", method = HttpMethod.POST, excludeFromRepeat = {2}),
				@Request(url = "test2/truck/[a-z]{10,20}", method = HttpMethod.POST)
			})
	public void multiRequestTest(
			@PathParam("service") String service,
			@PathParam("id") String id,
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName) {

		assertData(service, id, firstName, lastName);
	}

	/**
	 * This will be invoked by "annotations" placed on "multiRequestTest" method
	 *
	 * @param service
	 * @param id
	 */
	@POST
	@Path("test2/{service}/{id}")
	public void multiRequestTestExternalMethod(
			@PathParam("service") String service,
			@PathParam("id") String id) {

		assertTrue("Service should be car or truck", (service.equals("car") || service.equals("truck")));
		assertTrue("Id should be number", id != null && !id.isEmpty());
	}

	/**
	 * Single request test
	 *
	 * @param service
	 * @param id
	 * @param firstName
	 * @param lastName
	 */
	@Test
	@POST
	@Path("test/{service}/{id}")
	@Request(url = "test/[a-z]{10,20}/[0-9]{1,10}?firstName=Jon&lastName=Doe", method = HttpMethod.POST)
	public void singleRequestTest(
			@PathParam("service") String service,
			@PathParam("id") String id,
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName) {

		assertData(service, id, firstName, lastName);
	}

	/**
	 * Single request to external resource test
	 *
	 * @param response
	 */
	@Test
	@Request(url = Resource.HELLO_WORLD_PATH)
	public void singleRequestExternalResourceTest(Response response) {
		assertEquals("Status should equal", 200, response.getStatus());
		assertEquals("Response text should equal", Resource.MESSAGE, response.readEntity(String.class));
	}

	/**
	 * Multi request to external resource test
	 *
	 * @param response
	 */
	@Test
	@Requests(requests = {
		@Request(url = Resource.HELLO_WORLD_PATH),
		@Request(url = Resource.SERVICE_PATH)
	})
	public void multiRequestExternalResourceTest(Response response) {
		assertEquals("Status should equal", 200, response.getStatus());

		String resp = response.readEntity(String.class);
		assertTrue("Message should be: ", Resource.MESSAGE.equals(resp) || Service.RESPONSE_TEXT.equals(resp));
	}


	private void assertData(String service, String id, String firstName, String lastName) {
		assertTrue("Service should be string", service != null && !service.isEmpty());
		assertTrue("Id should be number", (Integer) Integer.parseInt(id) instanceof Integer);
		assertEquals("FirstName should equal", "Jon", firstName);
		assertEquals("LastName should equal", "Doe", lastName);
	}

}

```

### Request filter test
```
@RunWith(JerseyRequestTestRunner.class)
public class RequestFilterTest extends JerseyRequestTest {

	public static final String PATH = "filterChangedMethod";

	public RequestFilterTest() {
		this.configuration.registerInstances(new RequestFilter());
	}

	@POST
	@Path(PATH)
	public String post() {
		return "success";
	}

	@Test
	public void testmethod() {
		// executing GET request that should be changed to POST by filter
		String resp = target().path(PATH).request().get().readEntity(String.class);
		assertEquals("Filter should change request method", "success", resp);
	}

}
```

### Request params test
```
@RunWith(JerseyRequestTestRunner.class)
public class RequestParamsTest extends JerseyRequestTest {

	private static final String FIRST_NAME = "Jon";
	private static final String LAST_NAME = "Doe";
	private static final String STATE = "sleeping";

	@GET
	@Path("beanParam/{state}")
	public void beanParams(
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@PathParam("state") String state
	) {
		assertEquals("First Name should equal", FIRST_NAME, firstName);
		assertEquals("Last Name should equal", LAST_NAME, lastName);
		assertEquals("State should equal", STATE, state);
	}

	@Test
	public void testParams() {
		Response resp = target().path("beanParam").path(STATE)
				.queryParam("firstName", FIRST_NAME)
				.queryParam("lastName", LAST_NAME)
				.request().get();

		assertEquals("Status code should be 204", 204, resp.getStatus());
	}

}
```

### Resource test
```
@RunWith(JerseyRequestTestRunner.class)
public class ResourceTest extends JerseyRequestTest {

	public ResourceTest() {
		this.configuration.register(Resource.class);
		this.abstractBinder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@Test
	public void testResource() {
		String resp = target().path(Resource.PATH).request().get().readEntity(String.class);
		assertEquals("Message returned by request should equal", Resource.MESSAGE, resp);
	}

	@Test
	public void testResourceService() {
		String resp = target().path(Resource.PATH).path("service").request().get().readEntity(String.class);
		assertEquals("Message returned by request should equal", Service.RESPONSE_TEXT, resp);
	}

}
```
### Service test
```
@RunWith(JerseyRequestTestRunner.class)
public class ServiceTest extends JerseyRequestTest {

	private Service service;

	public ServiceTest() {
		this.abstractBinder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

	@Context
	public void setMyService(Service service) {
		this.service = Mockito.mock(Service.class, delegatesTo(service));
	}

	@Test
	public void messageTest() {
		Mockito.verify(service, times(0)).getText();
		assertEquals("Returned message should equal", Service.RESPONSE_TEXT, service.getText());
		Mockito.verify(service, times(1)).getText();
	}

	@Test
	public void zeroInteractionsTest() {
		Mockito.verifyZeroInteractions(service);
	}

	@Test
	public void customReturnTest() {
		assertNull("User should be null", service.getUser());
		Mockito.doReturn(new User()).when(service).getUser();
		assertNotNull("User should be not null", service.getUser());
	}
}
```

