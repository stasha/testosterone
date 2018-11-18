package info.stasha.testosterone.annotation;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for annotating test that should act like integration test.
 *
 * @author stasha
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Dependencies {

    /**
     * Test dependencies. List test classes needed for this test.
     */
    Class<? extends Testosterone>[] value() default {};
}
