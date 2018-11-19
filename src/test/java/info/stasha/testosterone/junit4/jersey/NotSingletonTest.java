package info.stasha.testosterone.junit4.jersey;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class NotSingletonTest implements Testosterone {

    public static boolean initialized;
    private static final String TEXT_STRING = "singleton test";

    private String str;
    private static String str1 = "default";
    private static String str2 = "default";

    @Before
    public void setUp() {
        if (!initialized) {
            str = TEXT_STRING;
            initialized = true;
        }
    }

    @AfterClass
    public static void tearDown() {
        assertNull("Text should be null", str1);
        assertEquals("Text should equal", TEXT_STRING, str2);
    }

    @Test
    public void test1() {
        assign(str);
    }

    @Test
    public void test2() {
        assign(str);
    }

    private void assign(String str) {
        if (str == null) {
            str1 = str;
        } else {
            str2 = str;
        }
    }
}
