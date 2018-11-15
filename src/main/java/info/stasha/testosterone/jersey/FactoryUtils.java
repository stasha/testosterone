package info.stasha.testosterone.jersey;

import java.lang.reflect.Method;
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
    public static Class<? extends Factory<Object>> mock(Class<? extends Factory<?>> clazz) {
        return create(clazz, new MockProvider<>());
    }

    /**
     * Returns instrumented class that will return spied object when "provide"
     * method is called. Supported only from Jersey version 2.4.
     *
     * @param clazz
     * @return
     */
    public static Class<? extends Factory<Object>> spy(Class<? extends Factory<?>> clazz) {
        return create(clazz, new SpyProvider<>());
    }

    /**
     * Instruments class by adding interceptor to provide method.
     *
     * @param clazz
     * @param provider
     * @return
     */
    private static Class<? extends Factory<Object>> create(Class<? extends Factory<?>> clazz, Object provider) {
        return (Class<? extends Factory<Object>>) new ByteBuddy()
                .subclass(clazz)
                .name(clazz.getName() + "_")
                .method(named("provide"))
                .intercept(MethodDelegation.to(provider))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();
    }

}
