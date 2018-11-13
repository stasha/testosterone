package info.stasha.testosterone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields or methods annotated with this annotation will be injected with
 * Mockito spy instead of real object.<br>
 * Note that spy will always create new mock object from the class of the real
 * object and delegate calls to the real object.
 *
 * @author stasha
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Spy {

}
