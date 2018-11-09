package info.stasha.testosterone.junit5;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.junit5.Testosterone.ContextInjectParameterResolver;
import info.stasha.testosterone.junit5.Testosterone.TestosteroneFactory;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * Testosterone "runner" for JUnit 5.
 *
 * TODO: Remove hard dependency on @Context and @Inject
 *
 * @author stasha
 */
@Extensions({
	@ExtendWith(TestosteroneFactory.class)
	, @ExtendWith(ContextInjectParameterResolver.class)})
public interface Testosterone extends info.stasha.testosterone.jersey.Testosterone {

	/**
	 * Factory for creating instrumented test classes
	 */
	public static class TestosteroneFactory implements TestInstanceFactory {

		/**
		 * {@inheritDoc }
		 *
		 * @param factoryContext
		 * @param extensionContext
		 * @return
		 * @throws TestInstantiationException
		 */
		@Override
		public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext)
				throws TestInstantiationException {
			try {
				return Instrument.testClass((Class<? extends Testosterone>) factoryContext.getTestClass(), new AfterAllAnnotation()).newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * Class for handling @Context and @Inject annotations as method arguments.
	 */
	public static class ContextInjectParameterResolver implements ParameterResolver {

		private Class<? extends Testosterone> cls;

		/**
		 * {@inheritDoc }
		 *
		 * @param pc
		 * @param rc
		 * @return
		 * @throws ParameterResolutionException
		 */
		@Override
		public Object resolveParameter(ParameterContext pc, ExtensionContext rc) throws ParameterResolutionException {
			return cls.cast(null);
		}

		/**
		 * {@inheritDoc }
		 *
		 * @param pc
		 * @param ec
		 * @return
		 * @throws ParameterResolutionException
		 */
		@Override
		public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) throws ParameterResolutionException {
			if (pc.getParameter().getType() == Response.class || pc.isAnnotated(Context.class) || pc.isAnnotated(Inject.class)) {
				cls = (Class<? extends Testosterone>) pc.getParameter().getType();
				return true;
			}
			return false;
		}
	}

}
