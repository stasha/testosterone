package info.stasha.testosterone.jersey.inject;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import javax.inject.Singleton;
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
    Testosterone test;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {

        AnnotatedElement el = injectee.getParent();
        Singleton singleton = el.getAnnotation(Singleton.class);

        if (singleton != null) {
            if (el instanceof Field) {
                try {
                    Field f = (Field) el;
                    f.setAccessible(true);
                    Object obj = f.get(test);

                    if (obj != null && obj.getClass().toString().toLowerCase().contains("mock")) {
                        locator.inject(obj);
                        return obj;
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    LOGGER.error("Failed to create mock " + injectee.getRequiredType().getTypeName(), ex);
                    throw new RuntimeException(ex);
                }
            }

        }

        try {

            Class<?> cls = Class.forName(injectee.getRequiredType().getTypeName());

            Object obj = locator.getService(cls);
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
