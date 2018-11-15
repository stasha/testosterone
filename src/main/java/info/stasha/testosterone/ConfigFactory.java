package info.stasha.testosterone;

import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Testosterone configuration factory interface.
 *
 * @author stasha
 */
public interface ConfigFactory {

	/**
	 * Map containing all Setup instances.
	 */
	Map<String, Setup> SETUP = new HashMap<>();

	/**
	 * Creates new Testosterone configuration.
	 *
	 * @return
	 */
	ServerConfig newServerConfig();

	/**
	 * Returns configuration for passed Testosterone object. If configuration
	 * does not exist, than it is created and returned.
	 *
	 * @param obj
	 * @return
	 */
	ServerConfig getServerConfig(Testosterone obj);

	/**
	 * Returns a db configuration.
	 *
     * @param obj
	 * @return
	 */
	DbConfig getDbConfig(Testosterone obj);

	/**
	 * Returns Setup object. If setup does not exist, than it is created and
	 * returned.
	 *
	 * @param obj
	 * @return
	 */
	Setup getSetup(Testosterone obj);

	/**
	 * Removes setup from map.
	 *
	 * @param obj
	 * @return
	 */
	Setup removeSetup(Testosterone obj);

	/**
	 * Returns TestExecutor instance.
	 *
	 * @param method
	 * @param target
	 * @return
	 */
	default TestExecutor getTestExecutor(Method method, Object target) {
		return new TestExecutorImpl(method, target);
	}
}
