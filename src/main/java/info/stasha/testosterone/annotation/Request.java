package info.stasha.testosterone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.HttpMethod;

/**
 * Request annotation based on which will "framework" send http requests.
 *
 * @author stasha
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
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
     * Url where to send request. If url is not provided, then annotated method
     * will be invoked.
     *
     * @return
     */
    String url() default "";

    /**
     * Request method
     *
     * @return
     */
    String method() default "";

    /**
     * Header parameters that will be used when invoking test method
     *
     * @return
     */
    String[] headerParams() default {};

    /**
     * Method name containing logic for asserting status code.
     *
     * @return
     */
    int[] expectedStatus() default {};

    /**
     * Expected status should be between including specified status codes.
     *
     * @return
     */
    int[] expectedStatusBetween() default {100, 400};

    /**
     * Field or method name where entity will be looked up.
     *
     * @return
     */
    String entity() default "";
}
