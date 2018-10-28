package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Interceptors
 *
 * @author stasha
 */
public class Interceptors {

	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final String className) {
		final List<Method> methods = new ArrayList<>();
		Class<?> klass = type;
		final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
		for (final Method method : allMethods) {
			for (Annotation anon : method.getAnnotations()) {
				if (anon.getClass().getSimpleName().equals(className)) {
					methods.add(method);
				}
			}
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
			 * @param method
			 * @param orig
			 * @return
			 * @throws Exception
			 */
			@RuntimeType
//			public static Object test(@Origin Method zuper, @This Testosterone orig) throws Exception {
			public static Object test(@SuperCall Callable<?> zuper, @Origin Method method, @This Testosterone orig) throws Throwable {
				try {
					return zuper.call();
				} catch (Throwable ex) {
					if (ex instanceof AssertionError) {
						MainTest.getMain(orig).getMain().getMessages().add(ex);
					} else {
						MainTest.getMain(orig).getMain().getExpectedExceptions().add(ex);
					}
				}
				return null;
			}
		}

	}

}
