package info.stasha.testosterone;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author stasha
 */
public class MyAppSuperTest extends JerseyRequestTest {

	@Override
	protected ResourceConfig configure() {
		return super.configure().register(HelloWorldResource.class);
	}

	@Override
	protected AbstractBinder configureAbstractBinder() {
		AbstractBinder ab = super.configureAbstractBinder();
		ab.bindFactory(TestFactory.class).to(MyService.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
		return ab;
	}

}
