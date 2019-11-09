package info.stasha.testosterone.cdi;

import java.util.logging.Logger;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.InjectionTargetFactory;

/**
 * CDI utils.
 *
 * @author stasha
 */
public class CdiUtils {

    public CdiUtils() {
    }

    private static final Logger LOGGER = Logger.getLogger(CdiUtils.class.getName());

    /**
     * Returns CDI BeanManager instance. If it is not available null is
     * returned.
     *
     * @return
     */
    public static BeanManager getBeanManager() {
        try {
            return CDI.current().getBeanManager();
        } catch (Exception ex) {
            LOGGER.info("There is no CDI (WELD) available. Injecting CDI beans will be skipped.");
            return null;
        }
    }

    public static Object create(Class<?> clazz) {
        BeanManager beanManager = getBeanManager();

        if (beanManager == null) {
            return null;
        }

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

        if (beanManager == null) {
            return null;
        }

        final AnnotatedType annotatedType = beanManager.createAnnotatedType(obj.getClass());
        final InjectionTargetFactory injectionTargetFactory = beanManager.getInjectionTargetFactory(annotatedType);
        final InjectionTarget injectionTarget = injectionTargetFactory.createInjectionTarget(null);

        final CreationalContext creationalContext = beanManager.createCreationalContext(null);
        injectionTarget.inject(obj, creationalContext);
        injectionTarget.postConstruct(obj);

        return obj;
    }

}
