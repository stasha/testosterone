package info.stasha.testosterone.junit4.annotations;

import info.stasha.testosterone.TestAnnotations;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class TestAnnotationsTest {

    private @interface TestAnnon {

    }

    @Test
    public void testAnnotation() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method m = TestAnnotations.class.getDeclaredMethod("getAnnotation", String.class);
        m.setAccessible(true);
        Object obj = m.invoke(null, "");
        assertNull("Method should return null value", obj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotation() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method m = TestAnnotations.class.getDeclaredMethod("get", String[].class);
        m.setAccessible(true);
        m.invoke(null, TestAnnon.class);
    }
}
