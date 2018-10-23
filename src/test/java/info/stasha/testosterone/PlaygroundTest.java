package info.stasha.testosterone;

import info.stasha.testosterone.JerseyRequestTestRunner;
import info.stasha.testosterone.JerseyWebRequestTest;
import info.stasha.testosterone.weblistener.MyInterceptor;
import info.stasha.testosterone.weblistener.WebListener;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import static net.bytebuddy.matcher.ElementMatchers.isDefaultConstructor;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Playground
 *
 * @author stasha
 */
@RunWith(JerseyRequestTestRunner.class)
public class PlaygroundTest extends JerseyWebRequestTest {

	@Override
	protected void prepareWebContainer() {
		Class<? extends WebListener> clazz;

		clazz = new ByteBuddy()
				.subclass(WebListener.class)
				.name("SampleSon")
				//				.defineConstructor(Visibility.PUBLIC)
				.constructor(isDefaultConstructor())
				.intercept(SuperMethodCall.INSTANCE.andThen(
						//				.intercept(
						MethodDelegation.to(MyInterceptor.class)
				))
				.make()
				.load(WebListener.class.getClassLoader())
				.getLoaded();

		this.servletContainer.addListener(clazz);

	}

	@Ignore
	@Test
	public void testWebListener() {
//		Mockito.verify(wl, times(1)).contextInitialized(Mockito.any(ServletContextEvent.class));
	}

}
