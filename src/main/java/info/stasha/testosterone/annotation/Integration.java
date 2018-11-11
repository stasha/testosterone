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
public @interface Integration {

	/**
	 * Testosterone test classes that should be used to build integration
	 * environment<br>
	 *
	 * Specified classes configurations will become a part of integration class.
	 *
	 * @return
	 */
	Class<? extends Testosterone>[] value() default {};
}
