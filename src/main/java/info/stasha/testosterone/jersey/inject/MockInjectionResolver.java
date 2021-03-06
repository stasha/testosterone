package info.stasha.testosterone.jersey.inject;

import info.stasha.testosterone.Utils;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

/**
 * Creates mock with Mockito.mock(class);
 *
 * @author stasha
 */
public class MockInjectionResolver implements InjectionResolver<Mock> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MockInjectionResolver.class);

    @Context
    ServiceLocator locator;

    @Context
    Testosterone test;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        AnnotatedElement el = injectee.getParent();

        Mock mock = el.getAnnotation(Mock.class);
        Singleton singleton = el.getAnnotation(Singleton.class);

        if (singleton != null) {
            if (el instanceof Field) {
                Field f = (Field) el;
                f.setAccessible(true);
                Object obj = Utils.getFieldValue(f, test);

                if (obj != null && obj.toString().toLowerCase().contains("mock")) {
                    return obj;
                }
            }
        }

        return Mockito.mock(Utils.getClass(injectee.getRequiredType().getTypeName()), mock.answer());

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
