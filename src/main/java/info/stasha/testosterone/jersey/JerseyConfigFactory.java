package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Setup;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.ConfigFactory;
import info.stasha.testosterone.Utils;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating Jersey server configuration.
 *
 * @author stasha
 */
public class JerseyConfigFactory implements ConfigFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(JerseyConfigFactory.class);

	/**
	 * Returns key used for storing setups.<br>
	 * The key is based on Testosterone class name.
	 *
	 * @param obj
	 * @return
	 */
	protected String getKey(Testosterone obj) {
		return obj.getClass().getName();
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public ServerConfig newServerConfig() {
		return new GrizzlyServerConfig();
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public ServerConfig getServerConfig(Testosterone obj) {
		return getSetup(obj).getServerConfig();
	}

	/**
	 * Returns DbConfig instance.
	 *
     * @param obj
	 * @return
	 */
	public DbConfig getDbConfig(Testosterone obj) {
		return new H2Config(obj);
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public Setup getSetup(Testosterone obj) {
		if (!SETUP.containsKey(getKey(obj))) {
			LOGGER.info("Creating new Setup for {}.", getKey(obj));
			try {
				ConfigFactory configFactory;
				ServerConfig serverConfig;

				Configuration ca = (Configuration) Utils.getAnnotation(obj, Configuration.class);
				if (ca != null) {
					configFactory = ca.configuration().newInstance();
					LOGGER.info("Creating new ConfigFactory {} specified in @Configuration.", configFactory.getClass().getName());

					serverConfig = configFactory.newServerConfig();
					serverConfig.setBaseUri(ca.baseUri());
					serverConfig.setPort(ca.port());
					serverConfig.setServerStarts(ca.serverStarts());
				} else {
					LOGGER.info("Creating default JettyConfigFactory.");
					configFactory = new JettyConfigFactory();
					serverConfig = configFactory.newServerConfig();
				}

				Setup setup = new Setup(obj, serverConfig);
				setup.setDbConfig(new H2Config(obj));

				SETUP.put(getKey(obj), setup);

			} catch (InstantiationException | IllegalAccessException ex) {
				LOGGER.error("Failed to create Setup for class {}", getKey(obj));
				throw new RuntimeException(ex);
			}
		}
		return SETUP.get(getKey(obj));
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public Setup removeSetup(Testosterone obj) {
		return SETUP.remove(getKey(obj));
	}

}
