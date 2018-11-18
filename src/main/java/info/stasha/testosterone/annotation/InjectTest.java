package info.stasha.testosterone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inject Testosterone test instance. Note that only classes that are specified in
 * @Integration or @Dependencies annotations can be injected.
 *
 * @author stasha
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectTest {

}
