package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.InvokeTest;
import info.stasha.testosterone.jersey.Testosterone;

/**
 * Test runner for running JerseyRequestTests.
 *
 * @author stasha
 */
public class TestosteroneRunner extends BlockJUnit4ClassRunner {

	protected Class<?> testClass;

	public static class Invoker extends Statement {

		private final Method method;
		private final Testosterone target;

		public Invoker(Method method, Object target) {
			this.method = method;
			this.target = (Testosterone) target;
		}

		@Override
		public void evaluate() throws Throwable {
			new InvokeTest(method, target).execute();
		}

	}

	public TestosteroneRunner(Class<?> clazz) throws Throwable {
		super(Instrument.testClass(clazz));
		this.testClass = clazz;
	}

	@Override
	protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation,
			boolean isStatic, List<Throwable> errors) {
		List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);

		for (FrameworkMethod eachTestMethod : methods) {
			if (annotation == Test.class) {
				eachTestMethod.validatePublicVoid(isStatic, errors);
			} else {
				eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
			}
		}
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object target) {
		return new Invoker(method.getMethod(), target);
	}

	@Override
	protected Description describeChild(FrameworkMethod method) {
		return Description.createTestDescription(this.testClass, testName(method), method.getAnnotations());
	}

}
