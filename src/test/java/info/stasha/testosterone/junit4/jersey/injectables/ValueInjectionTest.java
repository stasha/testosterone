package info.stasha.testosterone.junit4.jersey.injectables;

import info.stasha.testosterone.junit4.*;
import info.stasha.testosterone.annotation.Value;
import info.stasha.testosterone.jersey.Testosterone;
import org.glassfish.jersey.server.ResourceConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Value Injection test.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ValueInjectionTest implements Testosterone {

    @Override
    public void configure(ResourceConfig config) {
        config.property("text1", "text1 value");
        config.property("text2", "text2 value");
        config.property(Value.DEFAULT_PROPERTIES_FILE_LOCATION, "default.properties");
    }

    @Value(value = "app.name", propertiesPath = "app.properties")
    private String appName;

    @Value("app.name")
    private String defaultAppName;

    @Value("text1")
    private String text1;

    private String text2;

    @Value("text2")
    public void setText2(String text) {
        this.text2 = text;
    }

    @Test
    public void loadFromValuePropertiesPath() {
        assertEquals("App name shoud be Testosterone", "Testosterone", appName);
    }

    @Test
    public void loadFromDefaultPropertiesPath() {
        assertEquals("App name shoud be Default Testosterone", "Default Testosterone", defaultAppName);
    }

    @Test
    public void fieldValue() {
        assertEquals("Text should be text1 value", "text1 value", text1);
    }

    @Test
    public void setterValue() {
        assertEquals("Text should be text2 value", "text2 value", text2);
    }

}
