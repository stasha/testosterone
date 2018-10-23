package info.stasha.testosterone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container for grouping requests.
 *
 * @author stasha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Requests {

	/**
	 * Number of times to repeat all registered requests
	 *
	 * @return
	 */
	int repeat() default 1;

	/**
	 * Requests that will be invoked. Requests are invoked in order they are
	 * registered.
	 *
	 * @return
	 */
	Request[] requests() default {};
}
