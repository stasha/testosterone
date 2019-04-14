package info.stasha.testosterone.jersey;

import static info.stasha.testosterone.AbstractTestConfig.TEST_CONFIG;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;
import org.glassfish.jersey.internal.spi.ForcedAutoDiscoverable;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

/**
 * Jersey container listener executed when container starts. It is used for
 * joining different resource configs.
 *
 * @author stasha
 */
public class JerseyContainerListener implements ForcedAutoDiscoverable, ContainerLifecycleListener {

    @Context
    private ResourceConfig runtimeConfig;

    @Override
    public void configure(FeatureContext context) {
        context.register(JerseyContainerListener.class);
    }

    @Override
    public void onStartup(Container container) {
        if (!runtimeConfig.isRegistered(JerseyInitializationMarker.class)) {
            ResourceConfig newConfig = new ResourceConfig(runtimeConfig);
            ResourceConfig testConfig = (ResourceConfig) TEST_CONFIG.get().getApplication();

            testConfig.register(JerseyInitializationMarker.class);

            Map<String, Object> props = new HashMap<>(newConfig.getProperties());

            testConfig.getClasses().forEach(c -> {
                newConfig.register(c);
            });

            testConfig.getInstances().forEach(i -> {
                newConfig.register(i);
            });

            testConfig.getProperties().forEach((k, v) -> {
                props.put(k, v);
            });

            testConfig.setProperties(props);

            container.reload(testConfig);
        }
    }

    @Override
    public void onReload(Container container) {
    }

    @Override
    public void onShutdown(Container container) {
    }

}
