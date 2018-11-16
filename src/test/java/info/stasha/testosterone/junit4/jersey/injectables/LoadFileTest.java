package info.stasha.testosterone.junit4.jersey.injectables;

import info.stasha.testosterone.junit4.*;
import info.stasha.testosterone.jersey.Testosterone;
import org.junit.Test;
import org.junit.runner.RunWith;
import info.stasha.testosterone.annotation.LoadFile;
import java.io.InputStream;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Test @LoadFile annotation.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class LoadFileTest implements Testosterone {

    @LoadFile("test.txt")
    private InputStream is;

    @LoadFile("test.txt")
    private String text;

    @Test
    public void test() {
        assertNotNull("Input Stream should not be null", is);
        assertFalse("Text string should not be empty", text.isEmpty());
    }

}
