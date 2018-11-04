package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.jersey.Testosterone;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * Test runner for running JerseyRequestTests.
 *
 * @author stasha
 */
public class TestosteroneRunner extends BlockJUnit4ClassRunner {

	protected Class<?> testClass;
	protected Class<?> cls;

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
				System.out.println("Failed to invoke from runner");
				throw ex;
			}
		}

	}

	public TestosteroneRunner(Class<?> clazz) throws Throwable {
		super(Instrument.testClass(
				clazz,
				new BeforeAnnotation(),
				new AfterAnnotation(),
				new BeforeClassAnnotation(),
				new AfterClassAnnotation()));
		this.testClass = clazz;
		cls = Instrument.testClass(clazz,
				new BeforeAnnotation(),
				new AfterAnnotation(),
				new BeforeClassAnnotation(),
				new AfterClassAnnotation());
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

	protected Statement withBeforeClasses(Statement statement) {
		List<FrameworkMethod> befores = new TestClass(cls).getAnnotatedMethods(BeforeClass.class);
		List<FrameworkMethod> newBefores = new ArrayList<>();
		for (FrameworkMethod before : befores) {
			if (before.getDeclaringClass() == this.cls) {
				newBefores.add(0, before);
			} else {
				newBefores.add(before);
			}
		}
		return befores.isEmpty() ? statement : new RunBefores(statement, newBefores, null);
	}

	protected Statement withAfterClasses(Statement statement) {
		List<FrameworkMethod> afters = new TestClass(cls).getAnnotatedMethods(AfterClass.class);
		List<FrameworkMethod> newAfters = new ArrayList<>();
		for (FrameworkMethod after : afters) {
			if (after.getDeclaringClass() == this.cls) {
				newAfters.add(after);
			} else {
				newAfters.add(0, after);
			}
		}
		return afters.isEmpty() ? statement : new RunAfters(statement, newAfters, null);
	}

}
