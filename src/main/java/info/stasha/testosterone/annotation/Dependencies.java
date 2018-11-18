package info.stasha.testosterone.annotation;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation for adding dependencies to the test.
 * </p>
 * <p>
 * This annotation differs from @Integration in the way that class annotated
 * with @Dependencies annotation will include all mock configurations in
 * specified classes whereas @Integration will skip mock configurations.
 * </p>
 * <p>
 * Basically, @Dependencies is used on one end of the application<br>
 * and @Integration on the other.
 * </p>
 * <p>
 * For example, use @Dependencies when testing DAO class that has dependency on
 * other DAO like: address DAO depends on user DAO.
 * </p>
 *
 * @author stasha
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Dependencies {

    /**
     * Test dependencies. List test classes needed for this test.
     * @return 
     */
    Class<? extends Testosterone>[] value() default {};
}
