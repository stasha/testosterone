# testosterone
Testosterone enables Jersey usage directly in JUnit test classes. 
In other words, test classes become Jersey resource classes with full JUnit functionality support. 
Components can be injected using @Context or @Inject which is ideal for testing single component functionality. 

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
### Injectables test
```
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

