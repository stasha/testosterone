package info.stasha.testosterone.annotation;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation for annotating test that should act like integration (e2e) test.
 * </p>
 * <p>
 * This annotation differs from @Dependencies in the way that class annotated
 * with @Integration annotation will skip all mock configurations in specified
 * classes whereas @Dependencies will includes mock configurations.
 * </p>
 * <p>
 * Basically, @Integration is used on one end of the application and @Dependencies
 * on the other. 
 * </p>
 *
 *
 * @author stasha
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Integration {

    /**
     * Testosterone test classes that should be used to build integration
     * environment<br>
     *
     * Configurations excluding mock configurations from specified classes will
     * be used to build one big integration configuration.
     *
     * @return
     */
    Class<? extends Testosterone>[] value() default {};
}
