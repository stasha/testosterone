package info.stasha.testosterone.cdi;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * CDI configuration that is passed into "configure" method overriden in test.
 *
 * @author stasha
 */
public class CdiConfig {

    public CdiConfig() {
    }

    private final Set<BeanConfig> BEANS = new LinkedHashSet<>();

    /**
     * Mock bean.
     *
     * @param beanConfig
     * @return
     */
    public BeanConfig mock(BeanConfig beanConfig) {
        this.BEANS.add(beanConfig.setType(BeanConfig.BeanType.MOCK));
        return beanConfig;
    }

    /**
     * Mock class.
     *
     * @param clazz
     * @return
     */
    public BeanConfig mock(Class<?> clazz) {
        BeanConfig bc = new BeanConfig().setBean((Class<? super Object>) clazz).setType(BeanConfig.BeanType.MOCK);
        this.BEANS.add(bc);
        return bc;
    }

    /**
     * Spy bean.
     *
     * @param beanConfig
     * @return
     */
    public BeanConfig spy(BeanConfig beanConfig) {
        this.BEANS.add(beanConfig.setType(BeanConfig.BeanType.SPY));
        return beanConfig;
    }

    /**
     * Spy class.
     *
     * @param clazz
     * @return
     */
    public BeanConfig spy(Class<?> clazz) {
        BeanConfig bc = new BeanConfig().setBean((Class<? super Object>) clazz).setType(BeanConfig.BeanType.SPY);
        this.BEANS.add(bc);
        return bc;
    }

    /**
     * Returns all registered BeanConfig's
     *
     * @return
     */
    public Set<BeanConfig> getBeans() {
        return BEANS;
    }

}
