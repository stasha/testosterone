package info.stasha.testosterone.jersey.inject;

import info.stasha.testosterone.annotation.InjectTest;
import info.stasha.testosterone.jersey.IntegrationContainer;
import info.stasha.testosterone.jersey.Testosterone;
import javax.ws.rs.core.Configuration;
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

    @Context
    Configuration config;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        IntegrationContainer t = (IntegrationContainer) config.getProperty("tests");
        if (t != null) {
            Testosterone test = t.get(injectee.getRequiredType().getTypeName());
            locator.inject(test);
            return test;
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
