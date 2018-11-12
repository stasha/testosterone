package info.stasha.testosterone.jersey;

import info.stasha.testosterone.annotation.InjectTest;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;

/**
 * TODO: refactor this
 *
 * @author stasha
 */
public class TestInjectionResolver implements InjectionResolver<InjectTest> {
    
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
