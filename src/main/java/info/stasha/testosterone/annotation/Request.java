package info.stasha.testosterone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.HttpMethod;

/**
 * Request annotation based on which will "framework" send http requests.
 *
 * @author stasha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Request {

	/**
	 * Number of times request should be invoked
	 *
	 * @return
	 */
	int repeat() default 1;

	/**
	 * Excludes request invocation from specified repeats.
	 *
	 * @return
	 */
	int[] excludeFromRepeat() default {};

	/**
	 * Url where to send request
	 *
	 * @return
	 */
	String url() default "";

	/**
	 * Request method
	 *
	 * @return
	 */
	String method() default HttpMethod.GET;

	/**
	 * Header parameters that will be used when invoking test method
	 *
	 * @return
	 */
	String[] headerParams() default {};
}
