package info.stasha.testosterone.junit4.annotations;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.annotation.RequestAnnotation;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class RequestAnnotationTest {

    RequestAnnotation an = new RequestAnnotation();

    @Test
    public void newRequestAnnotationTest() {
        assertNotNull("Request annotation should not be null", new RequestAnnotation());
    }

    @Test
    public void requestAnnotationTypeTest() {
        assertEquals("Request annotation type should equal", Request.class, new RequestAnnotation().annotationType());
    }

    @Test
    public void testSertters() {
        an.setEntity("entity");
        an.setExcludeFromRepeat(new int[]{2});
        an.setExpectedStatus(new int[]{300});
        an.setExpectedStatusBetween(new int[]{1, 2});
        an.setHeaderParams(new String[]{"test:param"});
        an.setMethod("NOMETHOD");
        an.setRepeat(1);
        an.setUrl("empty");

        assertEquals("RequestAnnotation param should equal", "entity", an.entity());
        assertArrayEquals("RequestAnnotation param should equal", new int[]{2}, an.excludeFromRepeat());
        assertArrayEquals("RequestAnnotation param should equal", new int[]{300}, an.expectedStatus());
        assertArrayEquals("RequestAnnotation param should equal", new int[]{1, 2}, an.expectedStatusBetween());
        assertArrayEquals("RequestAnnotation param should equal", new String[]{"test:param"}, an.headerParams());
        assertEquals("RequestAnnotation param should equal", "NOMETHOD", an.method());
        assertEquals("RequestAnnotation param should equal", 1, an.repeat());
        assertEquals("RequestAnnotation param should equal", "empty", an.url());
    }
}
