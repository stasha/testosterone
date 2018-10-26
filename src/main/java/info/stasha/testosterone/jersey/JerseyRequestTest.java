package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Testosterone;
import org.glassfish.jersey.server.ResourceConfig;
// not supported from Jersey 2.26 
import org.glassfish.hk2.utilities.binding.AbstractBinder;
// supported from Jersey 2.26
//import org.glassfish.jersey.internal.inject.AbstractBinder;

/**
 *
 * @author stasha
 */
public abstract class JerseyRequestTest  implements Testosterone {

	protected ResourceConfig configuration;
	protected AbstractBinder abstractBinder;

	protected ResourceConfig configure() {

		if (this.configuration == null) {
			this.configuration = new ResourceConfig();
			this.abstractBinder = new AbstractBinder() {
				@Override
				protected void configure() {
				}
			};
		}

		this.configuration.registerInstances(this);
		this.configuration.register(this.abstractBinder);

		init();

		return this.configuration;

	}

	protected void init() {

	}

}
