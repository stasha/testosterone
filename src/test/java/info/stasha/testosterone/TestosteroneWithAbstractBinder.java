package info.stasha.testosterone;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author stasha
 */
public interface TestosteroneWithAbstractBinder extends Testosterone {

	@Override
	default void configure(ResourceConfig config) {
		AbstractBinder ab = new AbstractBinder() {
			@Override
			protected void configure() {

			}
		};
		config.register(ab);
		configure(config, ab);
	}

	default void configure(ResourceConfig config, AbstractBinder binder) {

	}
}
