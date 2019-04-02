package info.stasha.testosterone.jersey.junit4.runtimebinding;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

/**
 *
 * @author stasha
 */
@Provider
public class RuntimeBindingFeature implements ApplicationEventListener {

    public static final String STRING_FROM_RUNTIME_BINDED_FACTORY = "string from runtime binded feature";

    @Context
    private ServiceLocator locator;

    @Override
    public void onEvent(ApplicationEvent event) {
        ServiceLocatorUtilities.bind(locator, new AbstractBinder() {
            @Override
            protected void configure() {
                this.bindFactory(new Factory<String>() {
                    @Override
                    public String provide() {
                        return STRING_FROM_RUNTIME_BINDED_FACTORY;
                    }

                    @Override
                    public void dispose(String instance) {
                        // do nothing
                    }
                }).to(String.class);
            }
        });
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }

}
