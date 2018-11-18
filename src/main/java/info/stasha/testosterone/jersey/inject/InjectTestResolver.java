package info.stasha.testosterone.jersey.inject;

import info.stasha.testosterone.annotation.InjectTest;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;

/**
 * Injects testosterone testing object instance.
 *
 * @author stasha
 */
public class InjectTestResolver implements InjectionResolver<InjectTest> {

    @Context
    ServiceLocator locator;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        return locator.createAndInitialize((Class<?>)injectee.getRequiredType());
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
