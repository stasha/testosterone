package info.stasha.testosterone.jersey;

import info.stasha.testosterone.annotation.Value;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

/**
 * TODO: refactor this
 *
 * @author stasha
 */
public class ValueInjectionResolver implements InjectionResolver<Value> {

    private static Map<String, Map<Object, Object>> props = new HashMap<>();

    private static Map loadProperties(String propertiesPath) {
        Map rp = props.get(propertiesPath);
        if (rp == null) {
            propertiesPath = propertiesPath.startsWith("/") ? propertiesPath : "/" + propertiesPath;
            try (InputStream fi = ValueInjectionResolver.class.getResourceAsStream(propertiesPath)) {
                Properties p = new Properties();
                p.load(fi);
                props.put(propertiesPath, p);
                return p;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ValueInjectionResolver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ValueInjectionResolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new Properties();
    }

    @Context
    private Configuration configuration;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        if (String.class == injectee.getRequiredType()) {
            Value annotation = injectee.getParent().getAnnotation(Value.class);

            if (annotation != null) {
                String prop = annotation.value();
                String propertiesPath = annotation.propertiesPath();
                String defaultPropsLocation = (String) configuration.getProperty(Value.DEFAULT_PROPERTIES_FILE_LOCATION);
                String result = (String) loadProperties(propertiesPath).get(prop);
                if (result == null && defaultPropsLocation != null) {
                    result = (String) loadProperties(defaultPropsLocation).get(prop);
                }
                if (result == null) {
                    result = (String) configuration.getProperties().get(prop);
                }

                return result;
            }
        }
        return null;
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return true;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }
}
