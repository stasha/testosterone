# testosterone
Testosterone enables Jersey usage directly in JUnit test classes. 
In other words, test classes become Jersey resource classes with full JUnit functionality support. 
Components can be injected using @Context or @Inject which is ideal for testing single component functionality. 

# example
```
@RunWith(JerseyRequestTestRunner.class)
public class HelloWorldTest extends MyAppSuperTest {

	private static final String INTERNAL_RESOURCE_PATH = "internalTestResourceMethod";

	private MyService myService;

	@Context
	public void setMyService(MyService myService) {
		this.myService = Mockito.mock(MyService.class, delegatesTo(myService));
	}

	@RequestTest(expected = RuntimeException.class)
	public void test() {
		if (1 == 1) {
			throw new IllegalStateException("exception");
		}
		System.out.println(myService.getText());
	}

	@GET
	@Path(INTERNAL_RESOURCE_PATH)
	@Produces("text/plain")
	public String test1(@Context UriInfo uriInfo) {
		assertTrue(uriInfo.getRequestUri().toString().endsWith(INTERNAL_RESOURCE_PATH));
		return myService.getText();
	}

	@POST
	@Path("test")
	@RequestTest(entity = "my entity")
	public void testInternalTestResourceMethod(String postData) {
		assertEquals("Text should equal", "my entity", postData);
		Mockito.verify(myService, times(0)).getText();

		Response resp = target().path(INTERNAL_RESOURCE_PATH).request().get();
		String data = resp.readEntity(String.class);
		assertEquals("Messages should equal", MyService.RESPONSE_TEXT, data);

		Mockito.verify(myService, times(1)).getText();
	}

	@RequestTest
	public void testHelloWorldResource() {
		Response resp = target().path("helloworld").request().get();
		String data = resp.readEntity(String.class);
		assertEquals("Messages should equal", HelloWorldResource.MESSAGE, data);
	}

	@RequestTest
	public void test2() {
		System.out.println(myService.getText());
	}

	@Test(expected = RuntimeException.class)
	public void test3() {
		assertTrue(true);

		if (1 == 1) {
			throw new IllegalStateException("exception");
		}
	}
}
```
