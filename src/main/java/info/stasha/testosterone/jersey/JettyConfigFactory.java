package info.stasha.testosterone.jersey;

import info.stasha.testosterone.ServerConfig;

/**
 * Factory that creates Jetty configuration.
 *
 * @author stasha
 */
public class JettyConfigFactory extends JerseyConfigFactory {

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public ServerConfig newConfiguration() {
		return new JettyServerConfig();
	}

}
