package info.stasha.testosterone;

import info.stasha.testosterone.TestResponseBuilder.TestResponse;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.annotation.Requests;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class TestExecutorTest implements Testosterone {
    private static int entityInvocationCount = 0;
    
    @AfterClass
    public static void afterClass(){
        assertEquals("methodEntityTest should be invoked 2x", 2, entityInvocationCount);
    }

    public String myThrowingEntity() {
        throw new RuntimeException();
    }

    public String myStringEntity() {
        return "my entity";
    }
    
    public Entity myEntity(){
        return Entity.json("my entity");
    }

    @Test(expected = RuntimeException.class)
    @Request(entity = "test")
    public void fieldEntityException() {

    }

    @Path("entityTest")
    @POST
    public String textFromMyEntity(String txt) {
        return "response with " + txt;
    }

    @Test
    @Requests(requests = {
        @Request(url = "entityTest", method="POST", entity = "myEntity"),
        @Request(url = "entityTest", method="POST", entity = "myStringEntity")
    })
    public void methodEntityTest(Response resp) {
        assertEquals("Response text should equal", "response with my entity", resp.readEntity(String.class));
        entityInvocationCount++;
    }

    @Test(expected = RuntimeException.class)
    @Request(entity = "myThrowingEntity")
    public void methodEntityException() {

    }

    @Path("test")
    @GET
    @Test(expected = RuntimeException.class)
    @Request(url = "test")
    public void methodException(TestResponse resp) {
        // tests if testosterone engine will pass exception successfully to Unit framework.
        throw new IllegalArgumentException();
    }

}
