package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import info.stasha.testosterone.resteasy.RestEasyTestConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
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

    public static <T extends SuperTestosterone> TestConfig getConfig(T testosterone) {
        TestConfig config = TEST_CONFIGURATIONS.get(Utils.getInstrumentedClassName(testosterone));

        // returning existing config
        if (config != null) {
            return config;
        }

        Configuration conf = testosterone.getClass().getAnnotation(Configuration.class);

        try {
            if (conf != null) {
                Constructor con = conf.configuration().
                        getDeclaredConstructor(SuperTestosterone.class, Configuration.class);

                config = (TestConfig) con.newInstance(testosterone, conf);
            } else {
                if (testosterone instanceof info.stasha.testosterone.jersey.junit4.Testosterone) {
                    config = new JerseyTestConfig((info.stasha.testosterone.jersey.junit4.Testosterone) testosterone);
                } else if (testosterone instanceof info.stasha.testosterone.resteasy.junit4.Testosterone) {
                    config = new RestEasyTestConfig((info.stasha.testosterone.resteasy.junit4.Testosterone) testosterone);
                }
            }

            TEST_CONFIGURATIONS.put(Utils.getInstrumentedClassName(testosterone), config);

            return config;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.error("Failed to create Test Configuration.", ex);
            throw new RuntimeException(ex);
        }

    }
}
