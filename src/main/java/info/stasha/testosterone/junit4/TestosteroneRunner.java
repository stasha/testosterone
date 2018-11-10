package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.jersey.Testosterone;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test runner for running JerseyRequestTests.
 *
 * @author stasha
 */
public class TestosteroneRunner extends BlockJUnit4ClassRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestosteroneRunner.class);

	protected Class<? extends Testosterone> testClass;
	protected Class<? extends Testosterone> cls;

	public static class Invoker extends Statement {

		private final FrameworkMethod method;
		private final Testosterone target;

		public Invoker(FrameworkMethod method, Object target) {
			this.method = method;
			this.target = (Testosterone) target;
		}

		@Override
		public void evaluate() throws Throwable {
//			
			try {
				method.invokeExplosively(target, new Object[method.getMethod().getParameterCount()]);
			} catch (IllegalArgumentException ex) {
				LOGGER.error("Failed to invoke test {}.", method.getName());
				throw ex;
			}
		}

	}

	public TestosteroneRunner(Class<? extends Testosterone> clazz) throws Throwable {
		super(Instrument.testClass(clazz, new AfterClassAnnotation()));
		this.testClass = clazz;
		cls = Instrument.getInstrumentedClass(clazz);
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
		return new Invoker(method, target);
	}

	@Override
	protected Description describeChild(FrameworkMethod method) {
		return Description.createTestDescription(this.testClass, testName(method), method.getAnnotations());
	}

	@Override
	public void run(RunNotifier notifier) {
		RunListener l = new ExecutionListener();
		notifier.addListener(l);
		notifier.fireTestRunStarted(getDescription());
		super.run(notifier);
		notifier.removeListener(l);
	}

}
