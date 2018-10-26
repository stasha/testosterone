package info.stasha.testosterone;

import java.util.List;
import java.util.Set;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author stasha
 */
public interface Configuration {

	void start() throws Exception;

	void stop() throws Exception;

	String getBaseUri();

	Configuration get();

	ResourceConfig getResourceConfig();
	
	AbstractBinder getAbstractBinder();

	public static Configuration newInstance() {
		return newInstance("jetty-servlet");
	}

	public static Configuration newInstance(String type) {
		switch (type) {
			case "jetty-servlet":
				return new JettyConfiguration();
			default:
				return new JettyConfiguration();
		}
	}

	void set(Configuration configuration);

	void init(Object obj);

	Client client();

	WebTarget target();

	Set<Throwable> getMessages();

	List<Throwable> getExpectedExceptions();

}
