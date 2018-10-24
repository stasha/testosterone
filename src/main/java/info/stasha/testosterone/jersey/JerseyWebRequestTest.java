package info.stasha.testosterone.jersey;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;

/**
 * This class sets up WEB container for running tests.
 *
 * @author stasha
 */
public class JerseyWebRequestTest extends JerseyRequestTest {

	protected ServletDeploymentContext.Builder servletContainer;
	protected ServletDeploymentContext servletDeploymentContext;

	@Override
	protected TestContainerFactory getTestContainerFactory() {
		return new GrizzlyWebTestContainerFactory();
	}

	/**
	 * Invoked before servletContainer is build. This is the time when to setup
	 * servlet container and register @WebListeners or @WebFilter...
	 */
	protected void prepareWebContainer() {

	}

	@Override
	protected DeploymentContext configureDeployment() {
		super.configureDeployment();
		servletContainer = ServletDeploymentContext.forServlet(new ServletContainer(this.configuration));
		prepareWebContainer();
		servletDeploymentContext = this.servletContainer.build();
		return servletDeploymentContext;
	}

}
