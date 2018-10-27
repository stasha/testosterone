package info.stasha.testosterone.jerseyon;

import info.stasha.testosterone.junit.ExpectRequestException;
import info.stasha.testosterone.junit.InvokeRequest;
import info.stasha.testosterone.junit.Instrument;
import java.lang.annotation.Annotation;
import java.util.List;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Test runner for running JerseyRequestTests.
 *
 * @author stasha
 */
public class TestosteroneRunner extends BlockJUnit4ClassRunner {

	protected Class<?> testClass;

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
		return new InvokeRequest(method, target);
	}

	@Override
	protected Statement possiblyExpectingExceptions(FrameworkMethod method, Object target, Statement next) {
		Test annotation = method.getAnnotation(Test.class);
		Class<? extends Throwable> exception
				= (annotation.expected() == null || annotation.expected() == Test.None.class) ? null : annotation.expected();
		return exception != null ? new ExpectRequestException(method, next, target, exception) : next;
	}


	@Override
	protected Description describeChild(FrameworkMethod method) {
		return Description.createTestDescription(this.testClass, testName(method), method.getAnnotations());
	}


}
