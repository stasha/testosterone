package info.stasha.testosterone;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
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
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 *
 * @author stasha
 */
public class JerseyRequestTestRunner extends BlockJUnit4ClassRunner {

	// cache for instrumented classes
	private static final Map<Class<?>, Class<?>> CLASSES = new HashMap<>();

	/**
	 * Interceptor for all methods annotated with @RequestTest or @Path
	 * annotations.
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

		if (!CLASSES.containsKey(clazz)) {

			Class<?> cls = new ByteBuddy()
					.subclass(clazz)
					.name(clazz.getSimpleName())
					//
					// adding @Path annotation to all methods annotated with
					// @RequestTest excluding methods that already have @Path annotation
					.method(isAnnotatedWith(RequestTest.class).and(not(isAnnotatedWith(Path.class))))
					.intercept(MethodDelegation.to(MyServiceInterceptor.class))
					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
					.annotateMethod(new PathAnnotation())
					//
					// adding @GET annotation to all methods annotated with
					// @RequestTest excluding methods that are already annotated
					// with one of GET, POST, PUT, DELETE, PATCH, HEAD and OPTIONS
					//					.method(isAnnotatedWith(RequestTest.class)
					//							.and(not(isAnnotatedWith(GET.class)))
					//							.and(not(isAnnotatedWith(POST.class)))
					//							.and(not(isAnnotatedWith(PUT.class)))
					//							.and(not(isAnnotatedWith(DELETE.class)))
					//							.and(not(isAnnotatedWith(PATCH.class)))
					//							.and(not(isAnnotatedWith(HEAD.class)))
					//							.and(not(isAnnotatedWith(OPTIONS.class)))
					//					)
					//					.intercept(MethodDelegation.to(MyServiceInterceptor.class))
					//					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
					.annotateMethod(new GetAnnotation())
					//
					// intercepting all methods that have @Path annotation
					.method(isAnnotatedWith(Path.class))
					.intercept(MethodDelegation.to(MyServiceInterceptor.class))
					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
					//
					// building and loading class
					.make()
					.load(clazz.getClassLoader())
					.getLoaded();

			CLASSES.put(clazz, cls);
		}

		return CLASSES.get(clazz);
	}

	public JerseyRequestTestRunner(Class<?> clazz) throws Throwable {
		super(getClazz(clazz));
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> methods = new ArrayList<>();
		methods.addAll(getTestClass().getAnnotatedMethods(Test.class));
		methods.addAll(getTestClass().getAnnotatedMethods(RequestTest.class));
		return methods;
	}

	protected Class<? extends Throwable> getExpectedException(Annotation annotation) {
		if (annotation == null) {
			return null;
		}

		Class<? extends Throwable> expectedException;

		if (annotation instanceof RequestTest) {
			expectedException = ((RequestTest) annotation).expected();
		} else {
			expectedException = ((Test) annotation).expected();
		}

		if (expectedException == Test.None.class) {
			return null;
		}

		return expectedException;

	}

	protected boolean expectsException(Annotation annotation) {
		return getExpectedException(annotation) != null;
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object target) {
		if (method.getAnnotation(RequestTest.class) != null) {
			return new InvokeRequest(method, target);
		}

		return new InvokeMethod(method, target);
	}

	@Override
	protected Statement possiblyExpectingExceptions(FrameworkMethod method, Object target, Statement next) {
		Annotation annotation;
		if (method.getAnnotation(RequestTest.class) != null) {
			annotation = method.getAnnotation(RequestTest.class);
			return expectsException(annotation) ? new ExpectRequestException(method, next, target, getExpectedException(annotation)) : next;
		}

		annotation = method.getAnnotation(Test.class);
		return expectsException(annotation) ? new ExpectException(next, getExpectedException(annotation)) : next;

	}

	@Override
	protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
		List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(Before.class);
		if (method.getAnnotation(RequestTest.class) != null) {
			return befores.isEmpty() ? statement : new RunBeforesRequest(method, statement, befores, target);
		}
		return befores.isEmpty() ? statement : new RunBefores(statement, befores, target);
	}

	@Override
	protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
		List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(After.class);
		if (method.getAnnotation(RequestTest.class) != null) {
			return afters.isEmpty() ? statement : new RunAftersRequest(method, statement, afters, target);
		}
		return afters.isEmpty() ? statement : new RunAfters(statement, afters, target);

	}

	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {

		System.out.println("");
		System.out.println("Running test: " + method.toString());

		super.runChild(method, notifier);

	}

}
