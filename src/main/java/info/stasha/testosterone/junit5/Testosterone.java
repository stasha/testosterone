package info.stasha.testosterone.junit5;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.junit5.Testosterone.ContextParameterResolver;
import info.stasha.testosterone.junit5.Testosterone.TestosteroneFactory;

import javax.ws.rs.core.Context;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

@Extensions({@ExtendWith(TestosteroneFactory.class), @ExtendWith(ContextParameterResolver.class)})
public interface Testosterone extends info.stasha.testosterone.jersey.Testosterone {

	public static class TestosteroneFactory implements TestInstanceFactory {

		@Override
		public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext)
				throws TestInstantiationException {
			try {
				return Instrument.testClass(factoryContext.getTestClass()).newInstance();
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public static class ContextParameterResolver implements ParameterResolver {

		@Override
		public Object resolveParameter(ParameterContext parameterContext,
				ExtensionContext extensionContext) throws ParameterResolutionException {
			return new Object();
		}

		@Override
		public boolean supportsParameter(ParameterContext parameterContext,
				ExtensionContext extensionContext) throws ParameterResolutionException {
			return true;
		}

	}

}
