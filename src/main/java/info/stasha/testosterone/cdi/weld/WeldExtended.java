package info.stasha.testosterone.cdi.weld;

import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.cdi.BeanConfig;
import static info.stasha.testosterone.cdi.weld.WeldUtils.getBeanManager;
import java.util.function.Consumer;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.InjectionTarget;
import org.jboss.weld.bootstrap.event.WeldAfterBeanDiscovery;
import org.jboss.weld.bootstrap.event.WeldBeanConfigurator;
import org.jboss.weld.contexts.CreationalContextImpl;
import org.jboss.weld.environment.se.ContainerLifecycleObserver;
import org.jboss.weld.environment.se.Weld;
import org.mockito.Mockito;

/**
 * Extended weld functionality tailored for testosterone.
 *
 * @author stasha
 */
public class WeldExtended extends Weld {

    public WeldExtended(TestConfig config) {
        this.addContainerLifecycleObserver(ContainerLifecycleObserver.afterBeanDiscovery(new Consumer<WeldAfterBeanDiscovery>() {
            @Override
            public void accept(WeldAfterBeanDiscovery event) {
                for (BeanConfig bean : config.getCdiConfig().getBeans()) {

                    AnnotatedType annotatedType = getBeanManager().createAnnotatedType(bean.getBean());
                    InjectionTarget it = getBeanManager().createInjectionTarget(annotatedType);

                    WeldBeanConfigurator config = event.addBean()
                            .read(WeldUtils.getBeanManager().createAnnotatedType(bean.getBean()))
                            .beanClass(bean.getBean())
                            .scope(bean.getScope())
                            .priority(bean.getPriority())
                            .name(bean.getNamed())
                            .alternative(true);
                    if (bean.getType().equals(BeanConfig.BeanType.MOCK)) {
                        config.createWith((i) -> {
                            return WeldUtils.inject(Mockito.mock(bean.getBean()));
                        });
                    } else if (bean.getType().equals(BeanConfig.BeanType.SPY)) {
                        config.createWith((i) -> {
                            return WeldUtils.inject(Mockito.spy(WeldUtils.create(bean.getBean())));
                        });
                    }

                    config.destroyWith((instance, ctx) -> {
                        it.preDestroy(instance);
                        it.dispose(instance);

                        ((CreationalContextImpl) ctx).release();
                    });

                    if (bean.getQualifiers() != null) {
                        config.addQualifiers(bean.getQualifiers());
                    }
                }
            }
        }));

    }

}
