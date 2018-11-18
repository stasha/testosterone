package info.stasha.testosterone.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class RequestAnnotationTest {

    @Test
    public void newRequestAnnotationTest() {
        assertNotNull("Request annotation should not be null", new RequestAnnotation());
    }

    @Test
    public void requestAnnotationTypeTest() {
        assertEquals("Request annotation type should equal", Request.class, new RequestAnnotation().annotationType());
    }
}
