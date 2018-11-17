package info.stasha.testosterone.junit5;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

/**
 * Clean JUnit5 test
 *
 * @author stasha
 */
public class NonTestosteroneTest {

    private static boolean executed;

    @AfterAll
    public static void after() {
        Assert.assertTrue("Test shoud be executed", executed);
    }

    @Test
    public void notRunByTestosteroneTest() {
        executed = true;
    }

}
