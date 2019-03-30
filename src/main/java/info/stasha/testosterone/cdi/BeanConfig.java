package info.stasha.testosterone.cdi;

import java.lang.annotation.Annotation;
import javax.enterprise.context.Dependent;

/**
 * CDI Bean configuration that will be used for registering
 * mocks/spies/alternatives
 *
 * @author stasha
 */
public class BeanConfig {

    public BeanConfig() {
    }

    public static enum BeanType {
        MOCK,
        SPY
    }

    private BeanType type;
    private Class<? super Object> bean;
    private String named;
    private Annotation[] qualifiers;
    private Class<? extends Annotation> scope = Dependent.class;
    private int priority = Integer.MAX_VALUE;

    /**
     * Returns bean type. MOCK | SPY
     *
     * @return
     */
    public BeanType getType() {
        return type;
    }

    /**
     * Sets bean type. MOCK | SPY
     *
     * @param type
     * @return
     */
    public BeanConfig setType(BeanType type) {
        this.type = type;
        return this;
    }

    /**
     * Returns bean class.
     *
     * @return
     */
    public Class<? super Object> getBean() {
        return bean;
    }

    /**
     * Sets bean class.
     *
     * @param bean
     * @return
     */
    public BeanConfig setBean(Class<? super Object> bean) {
        this.bean = bean;
        return this;
    }

    /**
     * Returns name used in @Named qualifier.
     *
     * @return
     */
    public String getNamed() {
        return named;
    }

    /**
     * Sets name used in @Named qualifier.
     *
     * @param named
     * @return
     */
    public BeanConfig setNamed(String named) {
        this.named = named;
        return this;
    }

    /**
     * Returns bean qualifiers.
     *
     * @return
     */
    public Annotation[] getQualifiers() {
        return qualifiers;
    }

    /**
     * Sets bean qualifiers.
     *
     * @param qualifiers
     * @return
     */
    public BeanConfig setQualifiers(Annotation[] qualifiers) {
        this.qualifiers = qualifiers;
        return this;
    }

    /**
     * Returns bean scope.
     *
     * @return
     */
    public Class<? extends Annotation> getScope() {
        return scope;
    }

    /**
     * Sets bean scope.
     *
     * @param scope
     * @return
     */
    public BeanConfig setScope(Class<? extends Annotation> scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Returns bean priority.
     *
     * @return
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets bean priority.
     *
     * @param priority
     * @return
     */
    public BeanConfig setPriority(int priority) {
        this.priority = priority;
        return this;
    }

}
