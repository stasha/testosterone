package info.stasha.testosterone.annotation;

import info.stasha.testosterone.ConfigFactory;
import info.stasha.testosterone.Start;
import info.stasha.testosterone.jersey.JettyConfigFactory;
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
	Class<? extends ConfigFactory> configuration() default JettyConfigFactory.class;

	/**
	 * Server base uri.
	 *
	 * @return
	 */
	String baseUri() default "http://localhost/";

	/**
	 * Server port
	 *
	 * @return
	 */
	int port() default 9999;

	/**
	 * When to start/stop server.
	 *
	 * @see Start
	 * @return
	 */
	Start serverStarts() default Start.PER_CLASS;

}
