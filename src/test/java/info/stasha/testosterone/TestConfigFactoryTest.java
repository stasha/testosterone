package info.stasha.testosterone;

import info.stasha.testosterone.jersey.junit4.PlaygroundTest;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class TestConfigFactoryTest {

    private final String root = System.getProperty("user.dir") + "/" + "target/test-classes/META-INF/";
    private final Path servicePath = Paths.get(root, "services", "info.stasha.testosterone.TestConfig");
    private final Path moveServicePath = Paths.get(root, "info.stasha.testosterone.TestConfig");

    @After
    public void tearDown() throws IOException {
        System.clearProperty(TestConfig.DEFAULT_TEST_CONFIG_PROPERTY);
        TestConfigFactory.TEST_CONFIGURATIONS.clear();
        if (Files.exists(moveServicePath)) {
            Files.move(moveServicePath, servicePath);
        }
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

    @Test
    public void loadDefaultTestConfig() throws IOException {
        Files.move(servicePath, moveServicePath);
        TestConfig t = TestConfigFactory.getConfig(new PlaygroundTest());

        assertNotNull("TestConfig should not be null", t);
        assertTrue("TestConfig should be instance of JerseyTestConfig", (t instanceof JerseyTestConfig));
        Files.move(moveServicePath, servicePath);
    }

}
