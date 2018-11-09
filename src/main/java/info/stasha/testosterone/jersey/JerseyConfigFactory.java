package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Setup;
import info.stasha.testosterone.annotation.Configuration;
import java.util.logging.Level;
import java.util.logging.Logger;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.ConfigFactory;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2Config;

/**
 * Factory for creating Jersey server configuration.
 *
 * @author stasha
 */
public class JerseyConfigFactory implements ConfigFactory {

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
	 * @return
	 */
	@Override
	public DbConfig getDbConfig() {
		return new H2Config();
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
			try {
				Configuration ca = obj.getClass().getAnnotation(Configuration.class);
				if (ca == null) {
					//TODO: fix this so it searches all superclasses not just first one
					for (Class<?> cls : obj.getClass().getSuperclass().getInterfaces()) {
						if (cls.isAnnotationPresent(Configuration.class)) {
							ca = cls.getAnnotation(Configuration.class);
						}
					}
				}
				ConfigFactory configFactory;
				ServerConfig serverConfig;
				if (ca != null) {
					configFactory = ca.configuration().newInstance();

					serverConfig = configFactory.newServerConfig();
					serverConfig.setBaseUri(ca.baseUri());
					serverConfig.setPort(ca.port());
					serverConfig.setServerStarts(ca.serverStarts());
				} else {
					configFactory = new JettyConfigFactory();
					serverConfig = configFactory.newServerConfig();
				}

				Setup setup = new Setup(obj, serverConfig);
				setup.setDbConfig(new H2Config());

				SETUP.put(getKey(obj), setup);

			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(Testosterone.class.getName()).log(Level.SEVERE, null, ex);
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
