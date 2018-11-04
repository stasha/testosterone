package info.stasha.testosterone.junit4;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.Testosterone;
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
@RunWith(TestosteroneSuiteRunner.class)
@org.junit.runners.Suite.SuiteClasses({
	ResourceTest.class,
	RunPerTestClassSuite.class,
	RunPerTestMethodSuite.class
})
@Configuration(serverStarts = Configuration.ServerStarts.PER_CLASS)
public class RunTestsAndSuitesSuite implements Testosterone {

	@Override
	public void configure(ResourceConfig config) {
		config.register(Resource.class);
	}

	@Override
	public void configure(AbstractBinder binder) {
		binder.bindFactory(ServiceFactory.class).to(Service.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
	}

}
