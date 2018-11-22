package info.stasha.testosterone.jersey.junit4.jersey.request;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.annotation.Requests;
import info.stasha.testosterone.TestResponseBuilder.TestResponse;
import info.stasha.testosterone.jersey.junit4.jersey.resource.Resource;
import info.stasha.testosterone.jersey.junit4.jersey.service.Service;
import info.stasha.testosterone.jersey.junit4.jersey.service.ServiceFactory;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Request test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class RequestTest implements Testosterone {

    public static boolean afterServerStartInvoed;
    public static int afterServerStartCount = 0;
    public static boolean customPathTestInvoked;

    @Override
    public void configure(ResourceConfig config) {
        config.register(Resource.class);
    }

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @Override
    public void afterServerStart() throws Exception {
        afterServerStartInvoed = true;
        afterServerStartCount++;
    }

    @AfterClass
    public static void afterClass() {
        assertTrue("After server start should be called", afterServerStartInvoed);
        assertEquals("After server start count should equal 1", 1, afterServerStartCount);
        assertTrue("Custom path test should be invoked", customPathTestInvoked);
    }

    @Test
    @GET
    @Path("test")
    public void customPathTest() {
        customPathTestInvoked = true;
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
                @Request(url = "test/[a-z]{10,20}/[0-9]{1,10}?firstName=Jon&lastName=Doe")
                ,
				@Request(url = "test2/car/[a-z]{10,20}", method = HttpMethod.POST, excludeFromRepeat = {2})
                ,
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
    @Request(url = "test/[a-z]{10,20}/[0-9]{1,10}?firstName=Jon&lastName=Doe&noValue", method = HttpMethod.POST)
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
     * @param req
     * @param wt
     * @param response
     */
    @Test
    @Request(url = Resource.HELLO_WORLD_PATH, expectedStatus = 200)
    public void singleRequestExternalResourceTest(
            @Context Request req,
            @Context WebTarget wt,
            @Context Response response) {
        assertNotNull("Request should not be null", req);
        assertNotNull("WebTarget should not be null", wt);
        assertEquals("Status should equal", 200, response.getStatus());
        assertEquals("Response text should equal", Resource.MESSAGE, response.readEntity(String.class));
    }

    /**
     * Multi request to external resource test
     *
     * @param req
     * @param wt
     * @param response
     */
    @Test
    @Requests(requests = {
        @Request(url = Resource.HELLO_WORLD_PATH, expectedStatus = 200)
        ,
		@Request(url = Resource.SERVICE_PATH)
    })
    public void multiRequestExternalResourceTest(
            @Context Request req,
            @Context WebTarget wt,
            @Context Response response) {
        assertNotNull("Request should not be null", req);
        assertNotNull("WebTarget should not be null", wt);

        String resp = response.readEntity(String.class);

        switch (req.url()) {
            case Resource.HELLO_WORLD_PATH:
                assertTrue("Message should be: ", Resource.MESSAGE.equals(resp));
                break;
            case Resource.SERVICE_PATH:
                assertEquals("Status should equal", 200, response.getStatus());
                assertTrue("Message should be: ", Resource.MESSAGE.equals(resp) || Service.RESPONSE_TEXT.equals(resp));
                break;
            default:
                fail("Request url is wrong");
                break;
        }
    }

    @Test
    @Request(headerParams = "Content-Type, application/json")
    public void headerParamTest(@HeaderParam("Content-Type") String contentType) {
        System.out.println(contentType);
        assertEquals("Content type should equals", "application/json", contentType);
    }

    @Test
    @GET
    @Path("testResponse")
    @Request(url = "testResponse")
    public void testResponseTest(TestResponse resp) {
        assertNotNull("TestResponse should not be null", resp);
        assertNotNull("Request should not be null", resp.getRequest());
        assertNotNull("Response should not be null", resp.getResponse());
        assertNotNull("WebTarget should not be null", resp.getWebTarget());
        assertEquals("Repeat index should be 1", 1, resp.getRepeatIndex());
    }

    @Test
    @Request(headerParams = {"Content-Type, application/json", "User-Agent, testosterone"})
    public void multipleHeaderParamTest(
            @HeaderParam("Content-Type") String contentType,
            @HeaderParam("User-Agent") String userAgent) {

        assertEquals("Content-Type should equals", "application/json", contentType);
        assertEquals("User-Agent should equals", "testosterone", userAgent);
    }

    @Request
    @Requests
    @Test(expected = IllegalStateException.class)
    public void requestAndRequestsTest() {

    }

    @Requests
    @Test(expected = IllegalStateException.class)
    public void requestsEmptyTest() {

    }

    @Test
    @GET
    @Path("tst")
    @Request(url = "tst")
    public void indexRequest(Integer index) {
        assertEquals("Index should equal", (Integer) 1, index);
    }

    @Test(expected = AssertionError.class)
    @Request(url = "tst", expectedStatus = 300)
    public void expectedStatusError() {
    }

    @Test(expected = AssertionError.class)
    @Request(url = "tst", expectedStatusBetween = {0, 2})
    public void expectedStatesBetweenError() {
    }

    /**
     * Checks injected data.
     *
     * @param service
     * @param id
     * @param firstName
     * @param lastName
     */
    private void assertData(String service, String id, String firstName, String lastName) {
        assertTrue("Service should be string", service != null && !service.isEmpty());
        assertTrue("Id should be number", (Long) Long.parseLong(id) instanceof Long);
        assertEquals("FirstName should equal", "Jon", firstName);
        assertEquals("LastName should equal", "Doe", lastName);
    }

}
