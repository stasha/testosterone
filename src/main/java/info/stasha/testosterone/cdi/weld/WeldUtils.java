package info.stasha.testosterone.cdi.weld;

import java.util.logging.Logger;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.InjectionTargetFactory;
import org.jboss.weld.proxy.WeldClientProxy;

/**
 *
 * @author stasha
 */
public class WeldUtils {

    public WeldUtils() {
    }

    private static final Logger LOGGER = Logger.getLogger(WeldUtils.class.getName());

    /**
     * Returns CDI BeanManager instance. If it is not available null is
     * returned.
     *
     * @return
     */
    public static BeanManager getBeanManager() {
        try {
            return CDI.current().getBeanManager();
        } catch (IllegalStateException ex) {
            LOGGER.info("There is no CDI (WELD) available. Injecting CDI beans will be skipped.");
            return null;
        }
    }

    public static Object create(Class<?> clazz) {
        BeanManager beanManager = getBeanManager();

        final AnnotatedType annotatedType = beanManager.createAnnotatedType(clazz);
        final InjectionTargetFactory injectionTargetFactory = beanManager.getInjectionTargetFactory(annotatedType);
        final InjectionTarget injectionTarget = injectionTargetFactory.createInjectionTarget(null);

        final CreationalContext creationalContext = beanManager.createCreationalContext(null);
        Object obj = injectionTarget.produce(creationalContext);
        injectionTarget.inject(obj, creationalContext);
        injectionTarget.postConstruct(obj);

        return obj;
    }

    public static <T> T inject(T obj) {
        BeanManager beanManager = getBeanManager();

        final AnnotatedType annotatedType = beanManager.createAnnotatedType(obj.getClass());
        final InjectionTargetFactory injectionTargetFactory = beanManager.getInjectionTargetFactory(annotatedType);
        final InjectionTarget injectionTarget = injectionTargetFactory.createInjectionTarget(null);

        final CreationalContext creationalContext = beanManager.createCreationalContext(null);
        injectionTarget.inject(obj, creationalContext);
        injectionTarget.postConstruct(obj);

        return obj;
    }

    public static <T> T unwrap(T obj) {
        try {
            return (T) ((WeldClientProxy) obj).getMetadata().getContextualInstance();
        } catch (ClassCastException ex) {
            // it's not wrapped
            return obj;
        }
    }

}
