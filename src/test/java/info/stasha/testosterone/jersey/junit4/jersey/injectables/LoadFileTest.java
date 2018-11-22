package info.stasha.testosterone.jersey.junit4.jersey.injectables;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import org.junit.Test;
import org.junit.runner.RunWith;
import info.stasha.testosterone.annotation.LoadFile;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.annotation.Requests;
import info.stasha.testosterone.jersey.inject.InputStreamInjectionResolver;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.ServiceLocator;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test @LoadFile annotation.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class LoadFileTest implements Testosterone {

    private static int requestCount;
    private static int inputStreamCount;

    @Context
    ServiceLocator locator;

    @LoadFile("test.txt")
    public InputStream is;

    @LoadFile("test.txt")
    public String text;

    @LoadFile("info/stasha/testosterone/nested.txt")
    public String nested;

    @AfterClass
    public static void afterClass() {
        assertEquals("Request repeat count shoudl equal", 20, requestCount);
    }

    @Test
    public void test() {
        assertNotNull("Input Stream should not be null", is);
        assertEquals("Text string should equal", "test", text);
    }

    @Test
    @Path("nested")
    @POST
    @Request(entity = "nested", method = "POST")
    public void nested(String txt) {
        assertEquals("Text string should equal", "nested text", txt);
        assertFalse("Text string should not be empty", text.isEmpty());
    }

    @Test
    @POST
    @Path("test")
    @Request(url = "test", repeat = 5, entity = "text", method = "POST")
    public void test(String txt) {
        assertNotNull("Input Stream should not be null", is);
        assertFalse("Text string should not be empty", text.isEmpty());
    }

    @Test
    @POST
    @Path("test2")
    @Requests(repeat = 2, requests = {
        @Request(url = "test2", repeat = 5, entity = "text", method = "POST")
        ,
        @Request(url = "test2", repeat = 5, entity = "text", method = "POST"),})
    public void test2(String txt) {
        assertNotNull("Input Stream should not be null", is);
        assertFalse("Text string should not be empty", text.isEmpty());
        requestCount++;
    }

    @Test
    @POST
    @Path("inputStream")
    @Request(url = "inputStream", repeat = 2, entity = "is", method = "POST")
    public void inputStream(String txt) throws IOException {
        if (inputStreamCount == 0) {
            inputStreamCount++;
            assertEquals("Text from input stream should equal", "test", txt);
        } else {
            assertEquals("Text from input stream should empty string as it was already consumed", "", txt);
        }
        assertFalse("Text string should not be empty", text.isEmpty());
    }

    @Test
    public void testProps() {
        InputStreamInjectionResolver ir = new InputStreamInjectionResolver();
        assertFalse("Method should be false", ir.isMethodParameterIndicator());
        assertTrue("Constructor should be true", ir.isConstructorParameterIndicator());
    }

    @Test(expected = IllegalStateException.class)
    public void notSupportedType() {
        locator.createAndInitialize(LoadFileWrongType.class);
    }
    
    @Test(expected = RuntimeException.class)
    public void notExistingPath() {
        locator.createAndInitialize(LoadFileNotExistingPath.class);
    }

}
