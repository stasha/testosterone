package info.stasha.testosterone;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class TestInExecutionTest {

    @Test
    public void getServerThreadTest() {
        TestInExecution te = new TestInExecutionImpl(null, null, null, null, new Object[]{"a", "b"});
        assertNull("MainThreadTest should be null", te.getMainThreadTest());
        assertNull("ServerThreadTest should be null", te.getServerThreadTest());
        assertNull("Origin method should be null", te.getOriginMainThreadTestMethod());
        assertArrayEquals("Arguments should match", new Object[]{"a", "b"}, te.getArguments());
    }

}
