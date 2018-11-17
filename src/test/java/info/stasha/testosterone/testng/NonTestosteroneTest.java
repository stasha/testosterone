package info.stasha.testosterone.testng;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * Clean TestNG test running outside Testosterone.
 *
 * @author stasha
 */
public class NonTestosteroneTest {

    private boolean executed;

    @AfterClass
    public void after() {
        Assert.assertTrue(executed, "Test shoud be executed");
    }

    @Test
    public void notRunByTestosteroneTest() {
        executed = true;
    }

}
