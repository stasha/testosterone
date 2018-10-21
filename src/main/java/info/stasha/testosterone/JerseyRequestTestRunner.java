package info.stasha.testosterone;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;

/**
 * Test runner for running JerseyRequestTests.
 *
 * @author stasha
 */
public class JerseyRequestTestRunner extends BlockJUnit4ClassRunner {

	// cache for instrumented classes
	protected static final Map<Class<?>, Class<?>> CLASSES = new HashMap<>();
	protected final Object childrenLock = new Object();
	protected volatile Collection<FrameworkMethod> filteredChildren = null;

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

		if (!CLASSES.containsKey(clazz)) {

			Class<?> cls = new ByteBuddy()
					.subclass(clazz)
					.name(clazz.getName() + "_")
					//
					// adding @Path annotation to all methods annotated with
					// @Test excluding methods that already have @Path annotation
					.method(isAnnotatedWith(Test.class)
							.and(not(isAnnotatedWith(Path.class)))
							.and(not(isAnnotatedWith(DontIntercept.class)))
					)
					.intercept(MethodDelegation.to(MyServiceInterceptor.class))
					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
					.annotateMethod(new PathAnnotation())
					//
					// adding @GET annotation to all methods annotated with
					// @Test excluding methods that are already annotated
					// with one of GET, POST, PUT, DELETE, PATCH, HEAD and OPTIONS
					//					.method(isAnnotatedWith(Test.class)
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
					.method(isAnnotatedWith(Path.class)
							.and(not(isAnnotatedWith(DontIntercept.class)))
					)
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

	private Class<? extends Throwable> getExpectedException(Test annotation) {
		if (annotation == null || annotation.expected() == Test.None.class) {
			return null;
		} else {
			return annotation.expected();
		}
	}

	protected boolean expectsException(Test annotation) {
		return getExpectedException(annotation) != null;
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object target) {
		return new InvokeRequest(method, target);
	}

	@Override
	protected Statement possiblyExpectingExceptions(FrameworkMethod method, Object target, Statement next) {
		Test annotation = method.getAnnotation(Test.class);
		return expectsException(annotation) ? new ExpectRequestException(method, next, target, getExpectedException(annotation)) : next;
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
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		System.out.println("");
		System.out.println("Running test: " + method.toString());

		super.runChild(method, notifier);
	}

	@Override
	public void filter(Filter filter) throws NoTestsRemainException {
		synchronized (childrenLock) {
			List<FrameworkMethod> children = new ArrayList<>(getFilteredChildren());
			for (Iterator<FrameworkMethod> iter = children.iterator(); iter.hasNext();) {

				FrameworkMethod each = iter.next();

//				Description method = Description.createTestDescription(each.getClass(), each.getName());
//				Request req = Request.aClass(each.getClass()).filterWith(method);
//				Filter f = Filter.matchMethodDescription(method);

				String filteredClass = filter.describe();
				String currentClass = "Method " + describeChild(each).toString().replace("_)", ")");

				// hack to enable running single test from IDE
//				if (shouldRun(filter, each)) {
				if (filteredClass.equals(currentClass)) {
					try {
						filter.apply(each);
					} catch (NoTestsRemainException e) {
						iter.remove();
					}
				} else {
					iter.remove();
				}
			}
			filteredChildren = Collections.unmodifiableCollection(children);
			if (filteredChildren.isEmpty()) {
				throw new NoTestsRemainException();
			}
		}
	}

	protected Collection<FrameworkMethod> getFilteredChildren() {
		if (filteredChildren == null) {
			synchronized (childrenLock) {
				if (filteredChildren == null) {
					filteredChildren = Collections.unmodifiableCollection(getChildren());
				}
			}
		}
		return filteredChildren;
	}

	private volatile RunnerScheduler scheduler = new RunnerScheduler() {
		@Override
		public void schedule(Runnable childStatement) {
			childStatement.run();
		}

		@Override
		public void finished() {
			// do nothing
		}
	};

	protected void runChildren(final RunNotifier notifier) {
		final RunnerScheduler currentScheduler = scheduler;
		try {
			for (final FrameworkMethod each : getFilteredChildren()) {
				currentScheduler.schedule(new Runnable() {
					public void run() {
						JerseyRequestTestRunner.this.runChild(each, notifier);
					}
				});
			}
		} finally {
			currentScheduler.finished();
		}
	}

	protected Statement childrenInvoker(final RunNotifier notifier) {
		return new Statement() {
			@Override
			public void evaluate() {
				runChildren(notifier);
			}
		};
	}

}
