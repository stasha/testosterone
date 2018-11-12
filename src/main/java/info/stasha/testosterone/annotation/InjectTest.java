package info.stasha.testosterone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inject text from property files or from jersey configuration.
 *
 * @see info.stasha.testosterone.jersey.ValueInjectionResolver for
 * implementation.
 * @author stasha
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectTest {

}
