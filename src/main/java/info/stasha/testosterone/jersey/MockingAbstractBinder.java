package info.stasha.testosterone.jersey;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.utilities.binding.ScopedBindingBuilder;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
public abstract class MockingAbstractBinder extends AbstractBinder {

    public <T> ScopedBindingBuilder<T> bindSpy(T service) {
        return super.bind(Mockito.spy(service));
    }

    /**
     * Binds factory that is providing mock objects.
     *
     * @param <T>
     * @param factory
     * @param instance
     * @return
     */
    public <T> ServiceBindingBuilder<T> bindMockFactory(Class<? extends Factory<T>> factory, Class<? extends T> instance) {
        return bindFactory(Mock.<T>mockFactory(factory, instance));
    }

    /**
     * Binds factory that is providing spied objects.
     *
     * @param <T>
     * @param factory
     * @param instance
     * @return
     */
    public <T> ServiceBindingBuilder<T> bindSpyFactory(Class<? extends Factory<T>> factory, Class<? extends T> instance) {
        return bindFactory(Mock.<T>mockFactory(factory, instance));
    }

}
