package info.stasha.testosterone;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.ws.rs.Path;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.not;
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

	/**
	 * Interceptor for all methods annotated with @Test or @Path annotations.
	 */
	public static class MyServiceInterceptor {

		@RuntimeType
		public static Object intercept(@SuperCall Callable<?> zuper, @This JerseyRequestTest orig) throws Exception {
			try {
				Object obj = zuper.call();
				return obj;
			} catch (Throwable ex) {
				if (ex instanceof AssertionError) {
					orig.getMessages().add(ex);
				} else {
					orig.setThrownException(ex);
				}
			}
			return null;
		}
	}

	/**
	 * Returns instrumented proxy class
	 *
	 * @param clazz
	 * @return
	 */
	public static Class<?> getClazz(Class<?> clazz) {

		Class<?> cls = new ByteBuddy()
				.subclass(clazz)
				.name(clazz.getName() + "_")
				.method(isAnnotatedWith(Test.class)
						.and(not(isAnnotatedWith(Path.class)))
						.and(not(isAnnotatedWith(DontIntercept.class)))
				)
				.intercept(MethodDelegation.to(MyServiceInterceptor.class))
				.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
				.annotateMethod(new PathAnnotation())
				.annotateMethod(new GetAnnotation())
				.method(isAnnotatedWith(Path.class)
						.and(not(isAnnotatedWith(DontIntercept.class)))
				)
				.intercept(MethodDelegation.to(MyServiceInterceptor.class))
				.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
				.make()
				.load(clazz.getClassLoader())
				.getLoaded();

		return cls;
	}

	public JerseyRequestTestRunner(Class<?> clazz) throws Throwable {
		super(getClazz(clazz));
	}

	@Override
	protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation,
			boolean isStatic, List<Throwable> errors) {
		List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);

		methods.forEach((eachTestMethod) -> {
			if (annotation == Test.class) {
				eachTestMethod.validatePublicVoid(isStatic, errors);
			} else {
				eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
			}
		});
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
		return Description.createTestDescription(getTestClass().getJavaClass().getName().replaceFirst("_$", ""),
				testName(method), method.getAnnotations());
	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		System.out.println("");
		System.out.println("Running test: " + method.toString());

		super.runChild(method, notifier);
	}

}
