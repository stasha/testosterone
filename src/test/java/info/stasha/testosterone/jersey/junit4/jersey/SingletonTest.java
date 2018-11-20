package info.stasha.testosterone.jersey.junit4.jersey;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.inject.Singleton;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@Singleton
@RunWith(TestosteroneRunner.class)
public class SingletonTest implements Testosterone {

    public static boolean initialized;
    private static final String TEXT_STRING = "singleton test";

    private String str;

    @Before
    public void setUp() {
        if (!initialized) {
            str = TEXT_STRING;
            initialized = true;
        }
    }

    @Test
    public void test1() {
        assertEquals("Text should equal", TEXT_STRING, str);
    }

    @Test
    public void test2() {
        assertEquals("Text should equal", TEXT_STRING, str);
    }
}
