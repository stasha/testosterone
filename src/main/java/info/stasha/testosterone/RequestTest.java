package info.stasha.testosterone;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.Test;

/**
 * Annotation used for annotating JerseyReqeustTest test methods.
 *
 * @author stasha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequestTest {

	/**
	 * Expected exception that will be thrown by test method.
	 *
	 * @return
	 */
	Class<? extends Throwable> expected() default Test.None.class;

}
