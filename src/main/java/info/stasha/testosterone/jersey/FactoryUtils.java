package info.stasha.testosterone.jersey;

import info.stasha.testosterone.SuperTestosterone;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import static net.bytebuddy.matcher.ElementMatchers.named;
import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;

/**
 * Jersey Factory utils.
 *
 * @author stasha
 */
public class FactoryUtils {

    private static final Map<String, Class<?>> CLASSES = new HashMap<>();

    private FactoryUtils() {
    }

    /**
     * FactoryUtils provide interceptor.
     *
     * @param <T>
     */
    public static class MockProvider<T> {

        @RuntimeType
        public T provide(@SuperCall Callable<?> zuper, @Origin Method method, @This Factory factory) throws Exception {
            return (T) Mockito.mock(zuper.call().getClass());
        }
    }

    /**
     * Spy provide interceptor.
     *
     * @param <T>
     */
    public static class SpyProvider<T> {

        @RuntimeType
        public T provide(@SuperCall Callable<?> zuper, @Origin Method method, @This Factory factory) throws Exception {
            return (T) Mockito.spy(zuper.call());
        }
    }

    /**
     * Returns instrumented class that will return object mock when "provide"
     * method is called. Supported only from Jersey version 2.4.
     *
     * @param clazz
     * @return
     */
    public static <T> Class<? extends Factory<T>> mock(Class<? extends Factory<?>> clazz) {
        return create(clazz, new MockProvider<>());
    }

    /**
     * Returns instrumented class that will return spied object when "provide"
     * method is called. Supported only from Jersey version 2.4.
     *
     * @param clazz
     * @return
     */
    public static <T> Class<? extends Factory<T>> spy(Class<? extends Factory<?>> clazz) {
        return create(clazz, new SpyProvider<>());
    }

    /**
     * Instruments class by adding interceptor to provide method.
     *
     * @param clazz
     * @param provider
     * @return
     */
    private static <T> Class<? extends Factory<T>> create(Class<? extends Factory<?>> clazz, Object provider) {
        String key = clazz.getName() + "$" + provider.getClass().getName();

        if (!CLASSES.containsKey(key)) {
            
            Class<? extends Factory<T>> cls = (Class<? extends Factory<T>>) new ByteBuddy()
                    .subclass(clazz)
                    .name(key)
                    .method(named("provide"))
                    .intercept(MethodDelegation.to(provider))
                    .make()
                    .load(clazz.getClassLoader())
                    .getLoaded();
            CLASSES.put(key, cls);

        }

        return (Class<? extends Factory<T>>) CLASSES.get(key);
    }

}
