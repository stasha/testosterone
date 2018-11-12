package info.stasha.testosterone.jersey;

import java.util.concurrent.Callable;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import static net.bytebuddy.matcher.ElementMatchers.named;
import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
public class Mock {

    public static class MockFactory<T> {

        final Class<?> clazz;
        final Class<T> obj;

        public MockFactory(Class<?> clazz, Class<T> obj) {
            this.clazz = clazz;
            this.obj = obj;
        }

        @RuntimeType
        public T provide() {
            return (T) Mockito.mock(obj);
        }
    }

    public static class SpyFactory<T> extends MockFactory<T> {

        public SpyFactory(Class<?> clazz, Class<T> obj) {
            super(clazz, obj);
        }

        @RuntimeType
        public T provide(@SuperCall Callable<?> zuper) throws Exception {
            return (T) Mockito.spy(zuper.call());
        }

    }

    public static <T> Class<? extends Factory<T>> mockFactory(Class<? extends Factory<T>> clazz, Class<? extends T> obj) {
        return new ByteBuddy()
                .subclass(clazz)
                .name(clazz.getName() + "_")
                .method(named("provide"))
                .intercept(MethodDelegation.to(new MockFactory(clazz, obj)))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();
    }

    public static <T> Class<? extends Factory<T>> spyFactory(Class<? extends Factory<T>> clazz, Class<? extends T> obj) {
        return new ByteBuddy()
                .subclass(clazz)
                .method(named("provide"))
                .intercept(MethodDelegation.to(new SpyFactory(clazz, obj)))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();
    }

}
