package info.stasha.testosterone.junit5;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.junit5.Testosterone.ContextInjectParameterResolver;
import info.stasha.testosterone.junit5.Testosterone.TestosteroneFactory;
import javax.inject.Inject;
import javax.ws.rs.core.Context;

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
@Configuration(serverStarts = Configuration.ServerStarts.PER_TEST)
public interface Testosterone extends info.stasha.testosterone.jersey.Testosterone {

	public static class TestosteroneFactory implements TestInstanceFactory {

		@Override
		public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext)
				throws TestInstantiationException {
			try {
				return Instrument.testClass(
						factoryContext.getTestClass(),
						new BeforeEachAnnotation(),
						new AfterEachAnnotation(),
						new BeforeAllAnnotation(),
						new AfterAllAnnotation()
				).newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public static class ContextInjectParameterResolver implements ParameterResolver {

		private Class<?> cls;

		@Override
		public Object resolveParameter(ParameterContext pc, ExtensionContext rc) throws ParameterResolutionException {
			return cls.cast(null);
		}

		@Override
		public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) throws ParameterResolutionException {
			if (pc.isAnnotated(Context.class) || pc.isAnnotated(Inject.class)) {
				cls = pc.getParameter().getType();
				return true;
			}
			return false;
		}
	}

}
