package info.stasha.testosterone.jersey.junit4.jersey;

import info.stasha.testosterone.jersey.junit4.*;
import info.stasha.testosterone.TestResponseBuilder.TestResponse;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Http methods test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class HttpMethodsTest implements Testosterone {

    public static String test = "text";

    public static boolean getInvoked;
    public static boolean optionsInvoked;
    public static boolean headInvoked;
    public static boolean deleteInvoked;
    public static boolean postTestInvoked;
    public static boolean putTestInvoked;
    public static boolean deleteTestInvoked;
    public static boolean headTestInvoked;
    public static boolean optionsTestInvoked;
    public static String putText;
    public static String postText;

    @AfterClass
    public static void afterClass() {
        assertTrue("Get method should be invoked", getInvoked);
        assertTrue("Options method should be invoked", optionsInvoked);
        assertTrue("Head method should be invoked", headInvoked);
        assertTrue("Delete method should be invoked", deleteInvoked);
        assertTrue("Post test should be invoked", postTestInvoked);
        assertTrue("Put test should be invoked", putTestInvoked);
        assertTrue("Put test should be invoked", deleteTestInvoked);
        assertTrue("Put test should be invoked", headTestInvoked);
        assertTrue("Put test should be invoked", optionsTestInvoked);
        assertEquals("Put text should equal", test, putText);
        assertEquals("Post text should equal", test, postText);
    }

    @GET
    @Path("get")
    public String get() {
        return "get text";
    }

    @Test
    @Request(url = "get", method = "GET")
    public void get(TestResponse resp) {
        getInvoked = true;
        assertEquals("Method shoud match", "GET", resp.getRequest().method());
        assertEquals("Response text should match", "get text", resp.getResponse().readEntity(String.class));
    }

    @POST
    @Path("post")
    public String post(String text) {
        postText = text;
        return "post text";
    }

    @Test
    @Request(url = "post", method = "POST", entity = "test")
    public void post(TestResponse resp) {
        assertEquals("Method shoud match", "POST", resp.getRequest().method());
        assertEquals("Response text should match", "post text", resp.getResponse().readEntity(String.class));
    }

    @PUT
    @Path("put")
    public String put(String text) {
        putText = text;
        return "put text";
    }

    @Test
    @Request(url = "put", method = "PUT", entity = "test")
    public void put(TestResponse resp) {
        assertEquals("Method shoud match", "PUT", resp.getRequest().method());
        assertEquals("Response text should match", "put text", resp.getResponse().readEntity(String.class));
    }

    @DELETE
    @Path("delete")
    public String delete(String text) {
        deleteInvoked = true;
        return "delete text";
    }

    @Test
    @Request(url = "delete", method = "DELETE", entity = "test")
    public void delete(TestResponse resp) {
        assertEquals("Method shoud match", "DELETE", resp.getRequest().method());
        assertEquals("Response text should match", "delete text", resp.getResponse().readEntity(String.class));
    }

    @HEAD
    @Path("head")
    @Produces(MediaType.TEXT_PLAIN)
    public String head() {
        headInvoked = true;
        return "head text";
    }

    @Test
    @Request(url = "head", method = "HEAD")
    public void head(TestResponse resp) {
        assertEquals("Method shoud match", "HEAD", resp.getRequest().method());
        assertEquals("Response text should match", MediaType.TEXT_PLAIN, resp.getResponse().getHeaderString("Content-Type"));
    }

    @OPTIONS
    @Path("options")
    public Response options() {
        optionsInvoked = true;
        return Response.ok("test3 with OPTIONS, body content")
                .header("Allow", "GET")
                .header("Allow", "OPTIONS")
                .build();
    }

    @Test
    @Request(url = "options", method = "OPTIONS")
    public void options(TestResponse resp) {
        assertEquals("Method shoud match", "OPTIONS", resp.getRequest().method());
        assertTrue("Response header should contain GET", resp.getResponse().getHeaders().get("Allow").contains("GET"));
        assertTrue("Response header should contain OPTIONS", resp.getResponse().getHeaders().get("Allow").contains("OPTIONS"));
    }

    @POST
    @Path("test")
    @Test
    public void postTest() {
        postTestInvoked = true;
    }

    @PUT
    @Path("test")
    @Test
    public void putTest() {
        putTestInvoked = true;
    }

    @DELETE
    @Path("test")
    @Test
    public void deleteTest() {
        deleteTestInvoked = true;
    }

    @HEAD
    @Path("test")
    @Test
    public void headTest() {
        headTestInvoked = true;
    }

    @OPTIONS
    @Path("test")
    @Test
    public void optionsTest() {
        optionsTestInvoked = true;
    }

}
