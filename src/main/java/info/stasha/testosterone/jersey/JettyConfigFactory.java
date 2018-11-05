package info.stasha.testosterone.jersey;

import info.stasha.testosterone.TestosteroneConfig;

/**
 *
 * @author stasha
 */
public class JettyConfigFactory extends JerseyConfigFactory {

	@Override
	public TestosteroneConfig newConfiguration() {
		return new JettyConfiguration();
	}

}
