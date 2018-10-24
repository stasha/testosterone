package info.stasha.testosterone.jersey;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.ws.rs.Path;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

/**
 *
 * @author stasha
 */
@Path("")
public abstract class JerseyRequestTest extends JerseyTest {

	protected ResourceConfig configuration;
	protected AbstractBinder abstractBinder;

	private final Set<Throwable> messages = new LinkedHashSet<>();
	private Throwable thrownException;


	public Set<Throwable> getMessages() {
		return messages;
	}

	public Throwable getThrownException() {
		return thrownException;
	}

	public void setThrownException(Throwable thrownException) {
		this.thrownException = thrownException;
	}


	@Override
	protected ResourceConfig configure() {
		enable(TestProperties.LOG_TRAFFIC);
//		enable(TestProperties.DUMP_ENTITY);
		if (this.configuration == null) {
			this.configuration = new ResourceConfig();
			this.abstractBinder = new AbstractBinder() {
				@Override
				protected void configure() {
				}
			};
		}
		// registering test class as resource
		this.configuration.registerInstances(this);
		return this.configuration.register(this.abstractBinder);
	}

}
