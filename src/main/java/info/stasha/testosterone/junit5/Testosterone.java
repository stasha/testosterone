package info.stasha.testosterone.junit5;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.junit5.Testosterone.TestosteroneFactory;

@Extensions(@ExtendWith(TestosteroneFactory.class))
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
}
