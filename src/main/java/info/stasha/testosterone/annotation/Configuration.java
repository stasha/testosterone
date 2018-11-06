/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.annotation;

import info.stasha.testosterone.jersey.JettyServerConfig;
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
	 * When to start/stop server.
	 */
	public enum ServerStarts {
		/**
		 * Server is started/stopped based on parent.<br>
		 * This option will run server only once per suite, in case tests are
		 * run from suite. If there is no parent configuration, server will be
		 * started like PER_CLASS.<br>
		 * This can be overwritten by configuring individual classes in suite.
		 * Note that you will also have to change server port in case you are
		 * changing this configuration on individual classes inside suite.
		 */
		PARENT_CONFIGURATION,
		/**
		 * Server is started/stopped per test class.
		 */
		PER_CLASS,
		/**
		 * Server is started/stopped per test method.
		 */
		PER_TEST,
		/**
		 * Don't start the server.
		 */
		DONT_START
	}

	/**
	 * Configuration that will be used for running tests
	 *
	 * @return
	 */
	Class<?> configuration() default JettyServerConfig.class;

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
	 * @see ServerStarts
	 * @return
	 */
	ServerStarts serverStarts() default ServerStarts.PER_CLASS;

}
