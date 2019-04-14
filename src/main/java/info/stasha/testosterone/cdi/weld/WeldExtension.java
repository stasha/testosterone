package info.stasha.testosterone.cdi.weld;

import info.stasha.testosterone.AbstractTestConfig;
import info.stasha.testosterone.cdi.BeanConfig;
import info.stasha.testosterone.cdi.CdiUtils;
import static info.stasha.testosterone.cdi.CdiUtils.getBeanManager;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import org.jboss.weld.bootstrap.event.WeldAfterBeanDiscovery;
import org.jboss.weld.bootstrap.event.WeldBeanConfigurator;
import org.jboss.weld.contexts.CreationalContextImpl;
import org.mockito.Mockito;

/**
 * Extension executed by WELD container. It is used for registering mock and spy
 * alternatives.
 *
 * @author stasha
 */
public class WeldExtension implements Extension {

    void afterBeanDiscovery(@Observes WeldAfterBeanDiscovery event, BeanManager bm) {

        AbstractTestConfig ac = AbstractTestConfig.TEST_CONFIG.get();

        for (BeanConfig bean : ac.getCdiConfig().getBeans()) {

            AnnotatedType annotatedType = getBeanManager().createAnnotatedType(bean.getBean());
            InjectionTarget it = getBeanManager().createInjectionTarget(annotatedType);

            WeldBeanConfigurator config = event.addBean()
                    .read(bm.createAnnotatedType(bean.getBean()))
                    .beanClass(bean.getBean())
                    .scope(bean.getScope())
                    .priority(bean.getPriority())
                    .name(bean.getNamed())
                    .alternative(true);
            if (bean.getType().equals(BeanConfig.BeanType.MOCK)) {
                config.createWith((i) -> {
                    return CdiUtils.inject(Mockito.mock(bean.getBean()));
                });
            } else if (bean.getType().equals(BeanConfig.BeanType.SPY)) {
                config.createWith((i) -> {
                    return CdiUtils.inject(Mockito.spy(CdiUtils.create(bean.getBean())));
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
}
