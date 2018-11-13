package info.stasha.testosterone.jersey.inject;

import info.stasha.testosterone.annotation.Mock;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
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

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        try {
            return Mockito.mock(Class.forName(injectee.getRequiredType().getTypeName()));
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Failed to load class " + injectee.getRequiredType().getTypeName(), ex);
            throw new RuntimeException(ex);
        }
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
