package info.stasha.testosterone.jersey.testng;

import info.stasha.testosterone.TestInExecutionImpl;
import org.testng.annotations.Test;



/**
 *
 * @author stasha
 */
public class TestosteroneTest {
        
    @Test(expectedExceptions = RuntimeException.class)
    public void testObjectFactoryTest() {
        new Testosterone.TestObjectFactory().newInstance(TestInExecutionImpl.class);
    }
}
