package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Testosterone;
import java.util.LinkedHashSet;
import java.util.Set;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
// not supported from Jersey 2.26 
import org.glassfish.hk2.utilities.binding.AbstractBinder;
// supported from Jersey 2.26
//import org.glassfish.jersey.internal.inject.AbstractBinder;

/**
 *
 * @author stasha
 */
public abstract class JerseyRequestTest extends JerseyTest implements Testosterone {
	
	protected ResourceConfig configuration;
	protected AbstractBinder abstractBinder;

	private final Set<Throwable> messages = new LinkedHashSet<>();

	private Throwable thrownException;

	@Override
	public Set<Throwable> getMessages() {
		return messages;
	}

	@Override
	public Throwable getThrownException() {
		return thrownException;
	}

	@Override
	public void setThrownException(Throwable thrownException) {
		this.thrownException = thrownException;
	}

	@Override
	protected ResourceConfig configure() {
		enable(TestProperties.LOG_TRAFFIC);

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
