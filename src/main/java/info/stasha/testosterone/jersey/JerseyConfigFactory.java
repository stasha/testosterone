package info.stasha.testosterone.jersey;

import info.stasha.testosterone.TestosteroneConfig;
import info.stasha.testosterone.TestosteroneConfigFactory;
import info.stasha.testosterone.TestosteroneSetup;
import info.stasha.testosterone.annotation.Configuration;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stasha
 */
public class JerseyConfigFactory implements TestosteroneConfigFactory {

	protected String getKey(Testosterone obj) {
		return obj.getClass().getName();
	}

	public TestosteroneConfig newConfiguration() {
		return new JerseyConfiguration();
	}

	@Override
	public TestosteroneConfig getConfiguration(Testosterone obj) {
		return getSetup(obj).getConfiguration();
	}

	@Override
	public TestosteroneSetup getSetup(Testosterone obj) {
		if (!SETUP.containsKey(getKey(obj))) {
			try {
				Configuration ca = obj.getClass().getAnnotation(Configuration.class);
				if (ca == null) {
					for (Class<?> cls : obj.getClass().getSuperclass().getInterfaces()) {
						if (cls.isAnnotationPresent(Configuration.class)) {
							ca = cls.getAnnotation(Configuration.class);
						}
					}
				}
				TestosteroneConfig config;
				if (ca != null) {
					config = (TestosteroneConfig) ca.configuration().newInstance();
					config.setBaseUri(ca.baseUri());
					config.setPort(ca.port());
					config.setServerStarts(ca.serverStarts());
				} else {
					config = newConfiguration();
				}

				SETUP.put(getKey(obj), new TestosteroneSetup(obj, config, false));

			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(Testosterone.class.getName()).log(Level.SEVERE, null, ex);
				throw new RuntimeException(ex);
			}
		}
		return SETUP.get(getKey(obj));
	}

	@Override
	public TestosteroneSetup removeSetup(Testosterone obj) {
		return SETUP.remove(getKey(obj));
	}

}
