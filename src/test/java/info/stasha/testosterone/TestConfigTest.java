package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(configuration = CustomTestConfig.class)
public class TestConfigTest implements Testosterone {

    public static Setup setup;

    @Override
    public void beforeServerStart() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(this.getTestConfig().getSetup()).setTestInExecution(any());
    }

    @Test(expected = RuntimeException.class)
    public void testException() {
        // exception should be thrown before reacing method body
    }

}
