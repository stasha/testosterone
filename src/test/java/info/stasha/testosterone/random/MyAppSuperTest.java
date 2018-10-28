package info.stasha.testosterone.random;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.jersey.resource.Resource;
import info.stasha.testosterone.jersey.service.Service;
import info.stasha.testosterone.jersey.service.ServiceFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public abstract class MyAppSuperTest implements Testosterone {

	@Override
	public void configure(ResourceConfig config, AbstractBinder binder) {
		config.register(Resource.class);
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

}
