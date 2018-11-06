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
 *
 * @author stasha
 */
public interface TestosteroneConfig {

	String getBaseUri();

	void setBaseUri(String uri);

	Client client();

	List<Throwable> getExpectedExceptions();

	Set<Throwable> getMessages();

	ResourceConfig getResourceConfig();

	Testosterone getResourceObject();

	ServletContainerConfig getServletContainerConfig();

	Testosterone getTestObject();

	String getTestThreadName();

	void init() throws Exception;

	void initConfiguration(Testosterone obj);

	int getPort();

	Configuration.ServerStarts getServerStarts();

	void setPort(int port);

	void setResourceConfig(ResourceConfig resourceConfig);

	void setResourceObject(Testosterone resourceObject);

	void setServerStarts(Configuration.ServerStarts serverStarts);

	void setServletContainerConfig(ServletContainerConfig servletContainerConfig);

	void setTestObject(Testosterone testObject);

	void setTestThreadName(String testThreadName);

	void start() throws Exception;

	void stop() throws Exception;

	boolean isRunning();

	WebTarget target();

	void throwErrorMessage() throws Throwable;

	void throwExpectedException() throws Throwable;

}
