package info.stasha.testosterone.jersey;

import info.stasha.testosterone.junit.RunAftersRequest;
import info.stasha.testosterone.junit.RunBeforesRequest;
import info.stasha.testosterone.junit.ExpectRequestException;
import info.stasha.testosterone.junit.InvokeRequest;
import info.stasha.testosterone.instrumentation.Instrument;
import java.lang.annotation.Annotation;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Test runner for running JerseyRequestTests.
 *
 * @author stasha
 */
public class JerseyRequestTestRunner extends BlockJUnit4ClassRunner {

	protected Class<?> testClass;

	public JerseyRequestTestRunner(Class<?> clazz) throws Throwable {
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
	protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
		List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(Before.class);
		return befores.isEmpty() ? statement : new RunBeforesRequest(method, statement, befores, target);
	}

	@Override
	protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
		List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(After.class);
		return afters.isEmpty() ? statement : new RunAftersRequest(method, statement, afters, target);
	}

	@Override
	protected Description describeChild(FrameworkMethod method) {
		return Description.createTestDescription(this.testClass, testName(method), method.getAnnotations());
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		System.out.println("");
		System.out.println("Running test: " + method.toString());

		super.runChild(method, notifier);
	}

}
