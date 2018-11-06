package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stasha
 */
public interface TestosteroneConfigFactory {
	
	Map<String, TestosteroneSetup> SETUP = new HashMap<>();

	TestosteroneConfig getConfiguration(Testosterone obj);

	TestosteroneSetup getSetup(Testosterone obj);

	TestosteroneSetup removeSetup(Testosterone obj);
}
