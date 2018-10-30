package info.stasha.testosterone;

import info.stasha.testosterone.jersey.JerseyConfiguration;
import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.junit.Test;

/**
 * Interceptors
 *
 * TODO: Remove JUnit dependency
 *
 * @author stasha
 */
public class Interceptors {

	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final String className) {
		final List<Method> methods = new ArrayList<>();
		Class<?> klass = type;
		final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
		for (final Method method : allMethods) {
//			System.out.println(method.getName());
			if (method.getName().startsWith(className)) {
				methods.add(method);
			}
//			for (Annotation anon : method.getAnnotations()) {
//				System.out.println(anon.getClass().getName() + " : " + anon.annotationType().getName());
////				if (anon.annotationType().getName().toString().contains(className)) {
////					methods.add(method);
////				}
//			}
		}
		return methods;
	}

	public static void invokeInitialMethod(String methodName, Testosterone orig) {
		try {
			orig.getClass().getMethod(methodName).invoke(orig);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * PathAndTest class interceptors.
	 */
	public static class Intercept {

		/**
		 * Constructor interceptor
		 *
		 * @param orig
		 */
		@RuntimeType
		public static void constructor(@This Testosterone orig) {
			MainTest.getMain(orig).setMain(orig);
		}

		/**
		 * @Before annotation interceptor
		 */
		public static class Before {

			@RuntimeType
			public static void before(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {
				// running server so it can be used even in @Before method
				invokeInitialMethod("beforeTest", orig);

				zuper.call();
			}
		}

		/**
		 * @After annotation interceptor
		 */
		public static class After {

			@RuntimeType
			public static void after(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {
				// running server so it can be used even in @After method
				invokeInitialMethod("afterTest", orig);

				zuper.call();
			}
		}

		/**
		 * @After annotation interceptor
		 */
		public static class PathAndTest {

			/**
			 * Method interceptor
			 *
			 * @param zuper
			 * @param args
			 * @param method
			 * @param orig
			 * @return
			 * @throws Exception
			 */
			@RuntimeType
			public static Object test(@SuperCall Callable<?> zuper, @AllArguments Object[] args, @Origin Method method, @This Testosterone orig) throws Throwable {

				boolean isMain = Thread.currentThread().getName().contains("main");
				Test t = method.getAnnotation(Test.class);
				Class<? extends Throwable> expected = t != null ? t.expected() : null;
				Method m = orig.getClass().getMethod(method.getName(), method.getParameterTypes());
				JerseyConfiguration config = orig.getConfiguration();

				if (isMain) {
					Set<Throwable> messages = MainTest.getMain(orig).getMain().getMessages();
					List<Throwable> exception = MainTest.getMain(orig).getMain().getExpectedExceptions();

					try {
						invokeInitialMethod("beforeTest", orig);

						new InvokeTest(m, orig).execute();

						if (!messages.isEmpty()) {
							throw messages.iterator().next();
						}

						if (!exception.isEmpty()) {
							throw exception.iterator().next();
						}
					} finally {
						if (isMain) {
							messages.clear();
							exception.clear();

							config.setTestExecutedCount(config.getTestExecutedCount() + 1);

							if (config.getTestExecutedCount() == config.getTestCount()) {
								invokeInitialMethod("afterTest", orig);
							}

						}
					}

				} else {
					try {
						List<Method> me = getMethodsAnnotatedWith(orig.getClass(), m.getName() + "$accessor$");

						m = me.get(0);
						m.setAccessible(true);

						try {
							return m.invoke(orig, args);
						} catch (IllegalArgumentException ex) {
							System.out.println("Failed to invoke from interceptor");
							throw ex;
						}
					} catch (Throwable ex) {
						if (ex instanceof InvocationTargetException) {
							ex = ex.getCause();
						}

						if (ex instanceof AssertionError) {
							MainTest.getMain(orig).getMain().getMessages().add(ex);
						} else {
							MainTest.getMain(orig).getMain().getExpectedExceptions().add(ex);
						}
					}
				}

				return null;
			}
		}

	}

}
