package info.stasha.testosterone.resteasy;

import com.google.inject.Binder;
import com.google.inject.Module;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.servers.JettyServerConfig;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author stasha
 */
public class RestEasyApplication extends Application implements Module {

    private static Set<Class<?>> classes = new HashSet<>();
    private static Set<Object> singletons = new HashSet<>();
    private static Map<String, Object> props = new HashMap<>();

    public static Object test;

    private static Application app;

    public static Application getApplication() {
        if (app == null) {
            app = new RestEasyApplication();
        }
        return app;
    }

    public RestEasyApplication() {
        System.out.println("app initialized");
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Map<String, Object> getProperties() {
        return props;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(test.getClass());
        binder.bind(ServerConfig.class).to(JettyServerConfig.class).asEagerSingleton();
    }

}
