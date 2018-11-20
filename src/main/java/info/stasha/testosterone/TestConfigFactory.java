package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import info.stasha.testosterone.jersey.Testosterone;
import static info.stasha.testosterone.jersey.Testosterone.LOGGER;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author stasha
 */
public class TestConfigFactory {

    public static final Map<String, TestConfig> TEST_CONFIGURATIONS = new HashMap<>();

    public static <T extends Testosterone> TestConfig getConfig(T testosterone) {
        TestConfig config = TEST_CONFIGURATIONS.get(Utils.getInstrumentedClassName(testosterone));

        // returning existing config
        if (config != null) {
            return config;
        }

        Configuration conf = testosterone.getClass().getAnnotation(Configuration.class);

        try {
            if (conf != null) {
                Constructor con = conf.configuration().
                        getDeclaredConstructor(Testosterone.class, Configuration.class);

                config = (TestConfig) con.newInstance(testosterone, conf);
            } else {
                if (testosterone instanceof info.stasha.testosterone.jersey.Testosterone) {
                    config = new JerseyTestConfig((info.stasha.testosterone.jersey.Testosterone) testosterone);
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
