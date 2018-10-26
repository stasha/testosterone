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
public abstract class Configuration {

	abstract void start() throws Exception;

	abstract void stop() throws Exception;

	abstract String getBaseUri();

	abstract Configuration get();

	abstract ResourceConfig getResourceConfig();
	
	abstract AbstractBinder getAbstractBinder();

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

	abstract void set(Configuration configuration);

	abstract void init(Object obj);

	abstract Client client();

	abstract WebTarget target();

	abstract Set<Throwable> getMessages();

	abstract List<Throwable> getExpectedExceptions();

}
