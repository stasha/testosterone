package info.stasha.testosterone.jersey.junit4.jersey.async;

import java.util.concurrent.TimeUnit;
import javax.inject.Provider;
import javax.ws.rs.core.Context;
import javax.ws.rs.container.AsyncResponse;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import static org.glassfish.jersey.server.monitoring.RequestEvent.Type.RESOURCE_METHOD_START;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

/**
 *
 * @author stasha
 */
public class ApplicationListener implements ApplicationEventListener {

    @Context
    private Provider<AsyncResponse> ac;

    @Override
    public void onEvent(ApplicationEvent event) {

    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return (RequestEvent event) -> {
            if (event.getType() == RESOURCE_METHOD_START) {
                ac.get().setTimeout(10, TimeUnit.MILLISECONDS);
            }
        };
    }
}
