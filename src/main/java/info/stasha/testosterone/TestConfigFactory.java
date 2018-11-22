package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating different types of configurations.
 *
 * @author stasha
 */
public class TestConfigFactory {

    static final Logger LOGGER = LoggerFactory.getLogger(TestConfigFactory.class);
    public static final Map<String, TestConfig> TEST_CONFIGURATIONS = new HashMap<>();

    private TestConfigFactory() {
    }

    public static <T extends SuperTestosterone> TestConfig getConfig(T testosterone) {
        TestConfig config = TEST_CONFIGURATIONS.get(Utils.getInstrumentedClassName(testosterone));

        // returning existing config
        if (config != null) {
            return config;
        }

        ServiceLoader<TestConfig> loader = ServiceLoader.load(TestConfig.class);
        String systemTestProperty = System.getProperty(TestConfig.DEFAULT_TEST_CONFIG_PROPERTY);
        Configuration conf = testosterone.getClass().getAnnotation(Configuration.class);

        try {
            if (conf != null) {
                config = conf.configuration().newInstance();
            } else if (systemTestProperty != null) {
                config = (TestConfig) Class.forName(systemTestProperty).newInstance();
            } else if (loader.iterator().hasNext()) {
                config = loader.iterator().next();
            } else {
                config = new JerseyTestConfig();
            }

            config.setConfig(conf);
            config.setTestosterone(testosterone);

            TEST_CONFIGURATIONS.put(Utils.getInstrumentedClassName(testosterone), config);

            return config;
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | ClassNotFoundException ex) {
            LOGGER.error("Failed to create Test Configuration.", ex);
            throw new RuntimeException(ex);
        }

    }
}
