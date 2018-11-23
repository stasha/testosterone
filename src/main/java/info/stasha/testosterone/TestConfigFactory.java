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

        if (config != null) {
            return config;
        }

        Configuration conf = testosterone.getClass().getAnnotation(Configuration.class);

        config = Utils.loadConfig(TestConfig.DEFAULT_TEST_CONFIG_PROPERTY,
                TestConfig.class, JerseyTestConfig.class, testosterone);
        config.setConfig(conf);
        config.setTestosterone(testosterone);

        TEST_CONFIGURATIONS.put(Utils.getInstrumentedClassName(testosterone), config);

        return config;

    }
}
