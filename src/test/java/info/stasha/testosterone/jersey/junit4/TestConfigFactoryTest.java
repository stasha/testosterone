package info.stasha.testosterone.jersey.junit4;

import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.TestConfigFactory;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class TestConfigFactoryTest {
    
    @After
    public void tearDown(){
        System.clearProperty(TestConfig.DEFAULT_TEST_CONFIG_PROPERTY);
    }

    @Test(expected = Exception.class)
    public void loadConfigFromSystemExceptionTest() {
        System.setProperty(TestConfig.DEFAULT_TEST_CONFIG_PROPERTY, "fdsfsadf");
        try {
            TestConfigFactory.getConfig(new Testosterone() {
            });
        } finally {
            System.clearProperty(TestConfig.DEFAULT_TEST_CONFIG_PROPERTY);
            TestConfig t = TestConfigFactory.getConfig(new Testosterone() {
            });

            assertNotNull("TestConfig should not be null", t);
        }
    }

    @Test
    public void loadConfigFromSystemTest() {
        System.setProperty(TestConfig.DEFAULT_TEST_CONFIG_PROPERTY, JerseyTestConfig.class.getName());
        TestConfig t = TestConfigFactory.getConfig(new PlaygroundTest());

        assertNotNull("TestConfig should not be null", t);
    }

}
