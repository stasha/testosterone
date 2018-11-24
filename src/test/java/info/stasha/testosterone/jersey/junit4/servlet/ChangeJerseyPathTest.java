package info.stasha.testosterone.jersey.junit4.servlet;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ChangeJerseyPathTest implements Testosterone {

    private static boolean executed;

    @Override
    public void configure(ServletContainerConfig config) {
        config.setJaxRsPath("/jersey/*");
    }

    @AfterClass
    public static void afterClass() {
        assertTrue("Test should be executed", executed);
    }

    @Test
    public void isExecuted() {
        executed = true;
//        System.setProperty(ServletContainerConfig.JAX_RS_PATH_PROPERTY, "/test/*");
    
    }

}
