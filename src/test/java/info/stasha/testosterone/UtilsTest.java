package info.stasha.testosterone;

import info.stasha.testosterone.jersey.junit4.PlaygroundTest;
import info.stasha.testosterone.annotation.Configuration;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class UtilsTest implements InterfaceWithAnnotation {

    @Test(expected = RuntimeException.class)
    public void getClasstest() {
        Utils.getClass((String) null);
    }

    @Test(expected = RuntimeException.class)
    public void newInstanceFromClassTest() {
        Utils.newInstance((Class<?>) null);
    }

    @Test(expected = RuntimeException.class)
    public void readFieldValue() {
        Utils.getFieldValue(null, null);
    }

    @Test
    public void isTestosteroneClassTest() {
        assertFalse("Utils should return false", Utils.isTestosterone((Class<?>) null));
        assertFalse("Utils should return false", Utils.isTestosterone((Object) null));
    }

    @Test(expected = RuntimeException.class)
    public void getTestosteroneTest() {
        Utils.getTestosterone(null);
    }

    @Test(expected = RuntimeException.class)
    public void getTestosteroneTest2() {
        Utils.getTestosterone((Class<? extends SuperTestosterone>) this.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void getMethodStartingWithNameTest() {
        assertNull("Method starting with name should be null", Utils.getMethodStartingWithName(null, null));
    }

    @Test
    public void getMethodStartingWithNameTest2() {
        assertNull("Method starting with name should be null", Utils.getMethodStartingWithName(this.getClass(), "method", new Class[]{}));
    }

    @Test
    public void getAnnotatedMethodsTest() {
        assertEquals("Method should return zero length list", 0, Utils.getAnnotatedMethods(this.getClass(), (List)null).size());
    }

    @Test
    public void getAnnotationTest() {
        assertNotNull("Method should return annotation from interface", Utils.getAnnotation(this.getClass(), Configuration.class));
    }

    @Test(expected = RuntimeException.class)
    public void invokeOriginalMethod() throws IllegalAccessException, InvocationTargetException {
        Utils.invokeOriginalMethod(this.getClass().getDeclaredMethods()[0], new PlaygroundTest(), null);
    }
    
    @Test(expected = RuntimeException.class)
    public void copyFields()  {
        Utils.copyFields(null, null);
    }
    
    
}
