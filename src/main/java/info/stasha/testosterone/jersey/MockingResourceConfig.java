package info.stasha.testosterone.jersey;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
public class MockingResourceConfig extends ResourceConfig {

    protected <T> T createSpy(Class<?> componentClass) {
        try {
            return Mockito.spy((T) componentClass.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(MockingResourceConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected Object createMock(Object component) {
        return Mockito.mock(component.getClass());
    }

    public <T> T registerSpy(Class<?> componentClass) {
        T spy = createSpy(componentClass);
        super.register(spy);
        return spy;
    }

    public <T> T registerSpy(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
        T spy = createSpy(componentClass);
        super.register(spy, contracts);
        return spy;
    }

    public <T> T registerSpy(Class<?> componentClass, Class<?>... contracts) {
        T spy = createSpy(componentClass);
        super.register(spy, contracts);
        return spy;
    }

    public <T> T registerSpy(Class<?> componentClass, int bindingPriority) {
        T spy = createSpy(componentClass);
        super.register(spy, bindingPriority);
        return spy;
    }

    public <T> T registerSpy(T component) {
        T spy = Mockito.spy(component);
        super.register(spy);
        return spy;
    }

    public <T> T registerSpy(T component, Map<Class<?>, Integer> contracts) {
        T spy = Mockito.spy(component);
        super.register(spy, contracts);
        return spy;
    }

    public <T> T registerSpy(T component, Class<?>... contracts) {
        T spy = Mockito.spy(component);
        super.register(spy, contracts);
        return spy;
    }

    public <T> T registerSpy(T component, int bindingPriority) {
        T spy = Mockito.spy(component);
        super.register(spy, bindingPriority);
        return spy;
    }

    //mock
    public <T> T registerMock(Class<T> componentClass, Map<Class<?>, Integer> contracts) {
        T mock = Mockito.mock(componentClass);
        super.register(mock, contracts);
        return mock;
    }

    public <T> T registerMock(Class<T> componentClass, Class<?>... contracts) {
        T mock = Mockito.mock(componentClass);
        super.register(mock, contracts);
        return mock;
    }

    public <T> T registerMock(Class<T> componentClass, int bindingPriority) {
        T mock = Mockito.mock(componentClass);
        super.register(mock, bindingPriority);
        return mock;
    }

    public <T> T registerMock(Class<T> componentClass) {
        T mock = Mockito.mock(componentClass);
        super.register(mock);
        return mock;
    }

}
