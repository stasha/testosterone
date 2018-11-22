package info.stasha.testosterone;

import info.stasha.testosterone.jersey.JerseyTestConfig;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
public class CustomTestConfig extends JerseyTestConfig {
    
    private final Setup setup = Mockito.spy(super.getSetup());

    @Override
    public Setup getSetup() {
        return setup;
    }

}
