package info.stasha.testosterone.jerseyon;

import info.stasha.testosterone.junit.ExpectRequestException;
import info.stasha.testosterone.junit.InvokeRequest;
import info.stasha.testosterone.instrumentation.Instrument;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

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
	protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
		List<FrameworkMethod> list = getAnnotatedMethods(Before.class);
		return list.isEmpty() ? statement : new RunBefores(statement, list, target);
	}

	@Override
	protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
		List<FrameworkMethod> list = getAnnotatedMethods(After.class);
		return list.isEmpty() ? statement : new RunAfters(statement, list, target);
	}

	protected List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> cls) {
		List<FrameworkMethod> methods = new ArrayList<>();
		methods.addAll(getTestClass().getAnnotatedMethods(cls));
		for (Class<?> i : ClassUtils.getAllInterfaces(testClass)) {
			TestClass tc = new TestClass(i);
			methods.addAll(tc.getAnnotatedMethods(cls));
		}
		return methods;
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
