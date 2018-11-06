package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Setup;
import info.stasha.testosterone.annotation.Configuration;
import java.util.logging.Level;
import java.util.logging.Logger;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.ConfigFactory;

/**
 * Factory for creating Jersey configuration.
 *
 * @author stasha
 */
public class JerseyConfigFactory implements ConfigFactory {

	/**
	 * Returns key used for storing setups.
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
	public ServerConfig newConfiguration() {
		return new GrizzlyServerConfig();
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public ServerConfig getConfiguration(Testosterone obj) {
		return getSetup(obj).getConfiguration();
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
				ServerConfig config;
				if (ca != null) {
					config = (ServerConfig) ca.configuration().newInstance();
					config.setBaseUri(ca.baseUri());
					config.setPort(ca.port());
					config.setServerStarts(ca.serverStarts());
				} else {
					config = newConfiguration();
				}

				SETUP.put(getKey(obj), new Setup(obj, config, false));

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
