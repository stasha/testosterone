package info.stasha.testosterone;

import javax.ws.rs.client.Client;

/**
 *
 * @author stasha
 */
public interface Configuration {

	void start() throws Exception;

	void stop() throws Exception;

	String getBaseUri();

	Configuration get();
	
	void init();
	
	Client client();
	
}
