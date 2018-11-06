package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.util.List;
import java.util.Set;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Testosterone configuration interface.
 *
 * @author stasha
 */
public interface ServerConfig {

	/**
	 * Returns base uri.
	 *
	 * @return
	 */
	String getBaseUri();

	/**
	 * Sets base uri.
	 *
	 * @param uri
	 */
	void setBaseUri(String uri);

	/**
	 * Returns Jersey Client instance.
	 *
	 * @return
	 */
	Client client();

	/**
	 * List of expected exceptions that will be thrown while invoking JUnit
	 * test.
	 *
	 * @return
	 */
	List<Throwable> getExpectedExceptions();

	/**
	 * Error messages produced by failed tests.
	 *
	 * @return
	 */
	Set<Throwable> getMessages();

	/**
	 * Returns ResourceConfig instance.
	 *
	 * @return
	 */
	ResourceConfig getResourceConfig();

	/**
	 * Sets ResourceConfig.
	 *
	 * @param resourceConfig
	 */
	void setResourceConfig(ResourceConfig resourceConfig);

	/**
	 * Returns Testosterone instance created by Jersey on http request.
	 *
	 * @return
	 */
	Testosterone getResourceObject();

	/**
	 * Sets resource object. This is test class created by Jersey on http
	 * request.
	 *
	 * @param resourceObject
	 */
	void setResourceObject(Testosterone resourceObject);

	/**
	 * Returns servlet configuration object where context params, listeners,
	 * filters and servlets can be registered.
	 *
	 * @return
	 */
	ServletContainerConfig getServletContainerConfig();

	/**
	 * Sets servlet container config.
	 *
	 * @param servletContainerConfig
	 */
	void setServletContainerConfig(ServletContainerConfig servletContainerConfig);

	/**
	 * Returns Testosterone instance that JUnit framework created for testing.
	 *
	 * @return
	 */
	Testosterone getTestObject();

	/**
	 * Sets test object created by JUnit when invoking test.
	 *
	 * @param testObject
	 */
	void setTestObject(Testosterone testObject);

	/**
	 * Thread name where JUnit test is running.
	 *
	 * @return
	 */
	String getTestThreadName();

	/**
	 * Sets thread name in which JUnit test runs.
	 *
	 * @param testThreadName
	 */
	void setTestThreadName(String testThreadName);

	/**
	 * Initialize configuration.
	 *
	 * @throws Exception
	 */
	void init() throws Exception;

	/**
	 * Initialize configuration for specified Testosterone object.
	 *
	 * @param obj
	 */
	void initConfiguration(Testosterone obj);

	/**
	 * Returns port on whic server will run.
	 *
	 * @return
	 */
	int getPort();

	/**
	 * Sets port on which server will run.
	 *
	 * @param port
	 */
	void setPort(int port);

	/**
	 * Returns enumeration value how server should run.
	 *
	 * @return
	 */
	Configuration.ServerStarts getServerStarts();

	/**
	 * Sets when server should start.
	 *
	 * @param serverStarts
	 */
	void setServerStarts(Configuration.ServerStarts serverStarts);

	/**
	 * Starts server.
	 *
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * Stops server.
	 *
	 * @throws Exception
	 */
	void stop() throws Exception;

	/**
	 * Returns true false if server is running.
	 *
	 * @return
	 */
	boolean isRunning();

	/**
	 * Returns WebTarget instance for invoking http requests.
	 *
	 * @return
	 */
	WebTarget target();

	/**
	 * Throws error exception produced by JUnit test.
	 *
	 * @throws Throwable
	 */
	void throwErrorMessage() throws Throwable;

	/**
	 * Throws expected exception expected by JUnit framework.
	 *
	 * @throws Throwable
	 */
	void throwExpectedException() throws Throwable;

}
