package info.stasha.testosterone.jersey;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Class used as a flag used for checking if Jersey testosterone config is
 * loaded by runtime.
 *
 * @author stasha
 */
public class JerseyInitializationMarker implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        return false;
    }

}
