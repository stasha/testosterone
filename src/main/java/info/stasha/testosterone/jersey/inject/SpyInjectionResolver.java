package info.stasha.testosterone.jersey.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import static org.mockito.AdditionalAnswers.delegatesTo;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inject spy object created by Mockito.mock(class, delegatesTo(obj));
 *
 * @author stasha
 */
public class SpyInjectionResolver implements InjectionResolver<Spy> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpyInjectionResolver.class);

    @Context
    ServiceLocator locator;

    @Context
    Configuration config;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {

        try {
            Object test =  config.getProperty("test");
            Class<?> cls = Class.forName(injectee.getRequiredType().getTypeName());

            Object obj = locator.getService(cls);
//            if (injectee.getParent() instanceof Field) {
//                Field f = (Field) injectee.getParent();
//                obj = test;
//                f = test.getClass().getSuperclass().getDeclaredField(f.getName());
//                f.setAccessible(true);
//                if (f.get(obj) != null) {
//                    return config.getProperty("test");
//                }
//            }
            if (obj instanceof Proxy) {

                Class<?> nonProxy = Class.forName(obj.toString().substring(0, obj.toString().indexOf("@")));
                if (Factory.class.isAssignableFrom(nonProxy)) {
                    Factory factory = (Factory) nonProxy.newInstance();
                    obj = factory.provide();
                }
            }

            if (obj == null) {
                throw new InstantiationException();
            }

            return Mockito.mock(cls, delegatesTo(obj));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.error("Failed to create spy for: " + injectee.getRequiredType() + ""
                    + "\n\n- Mybee you forgot to bind service or factory in AbstractBinder?\n\n", ex);
            throw new RuntimeException(ex);
//        } catch (NoSuchFieldException ex) {
//            java.util.logging.Logger.getLogger(SpyInjectionResolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(SpyInjectionResolver.class.getName()).log(Level.SEVERE, null, ex);
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
