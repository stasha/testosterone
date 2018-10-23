package info.stasha.testosterone;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.ws.rs.Path;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.internal.ArrayComparisonFailure;
import org.junit.internal.ExactComparisonCriteria;
import org.junit.internal.InexactComparisonCriteria;

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

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected DeploymentContext configureDeployment() {
		ResourceConfig config = configure();

		// registering test class as resource
		config.registerInstances(this);

		return DeploymentContext.builder(config).build();
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
		return this.configuration.register(this.abstractBinder);
	}


}
