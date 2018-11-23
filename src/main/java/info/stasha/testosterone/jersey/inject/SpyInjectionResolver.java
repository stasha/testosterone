package info.stasha.testosterone.jersey.inject;

import info.stasha.testosterone.Utils;
import info.stasha.testosterone.jersey.junit4.Testosterone;
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
                Field f = (Field) el;
                f.setAccessible(true);
                Object obj = Utils.getFieldValue(f, test);

                if (obj != null && obj.getClass().toString().toLowerCase().contains("mock")) {
                    locator.inject(obj);
                    return obj;
                }

            }

        }

        Class<?> cls = Utils.getClass(injectee.getRequiredType().getTypeName());

        Object obj = locator.getService(cls);
        if (obj instanceof Proxy) {

            Class<?> nonProxy = Utils.getClass(obj.toString().substring(0, obj.toString().indexOf("@")));
            if (Factory.class.isAssignableFrom(nonProxy)) {
                Factory factory = Utils.newInstance(nonProxy);
                obj = factory.provide();
            }
        }

        return Mockito.mock(cls, delegatesTo(obj));

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
