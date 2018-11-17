package info.stasha.testosterone.annotation;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.configs.DefaultTestConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration for configuring tests.
 *
 * @author stasha
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Configuration {

	

	/**
	 * Configuration that will be used for running tests
	 *
	 * @return
	 */
	Class<? extends TestConfig> configuration() default DefaultTestConfig.class;

	/**
	 * Server base uri.
	 *
	 * @return
	 */
	String baseUri() default TestConfig.BASE_URI;

	/**
	 * Server httpPort
	 *
	 * @return
	 */
	int httpPort() default TestConfig.HTTP_PORT;

	/**
	 * When to start/stop server.
	 *
	 * @see StartServer
	 * @return
	 */
	StartServer startServer() default StartServer.PER_CLASS;

}
