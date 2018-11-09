package info.stasha.testosterone.jersey;

import info.stasha.testosterone.ServerConfig;

/**
 * Factory for creating Jetty server configuration.
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
	public ServerConfig newServerConfig() {
		return new JettyServerConfig();
	}

}
