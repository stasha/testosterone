package info.stasha.testosterone.interceptors;

import info.stasha.testosterone.jerseyon.TestosteroneMain;
import info.stasha.testosterone.jerseyon.Testosterone;
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
import org.junit.Before;
import org.junit.Test;

/**
 * Interceptors
 *
 * TODO: Remove JUnit dependencies
 *
 * @author stasha
 */
public class Interceptors {

	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
		final List<Method> methods = new ArrayList<>();
		Class<?> klass = type;
		final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
		for (final Method method : allMethods) {
			if (method.isAnnotationPresent(annotation)) {
				Annotation annotInstance = method.getAnnotation(annotation);
				methods.add(method);
			}
		}
		return methods;
	}

	public static void invokeInitialMethod(String methodName, Testosterone orig, boolean check) {
		try {
			if (check && getMethodsAnnotatedWith(orig.getClass(), Before.class).isEmpty()) {
				orig.getClass().getMethod(methodName).invoke(orig);
			} else {
				orig.getClass().getMethod(methodName).invoke(orig);
			}

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
			TestosteroneMain.getMain(orig).setMain(orig);
		}

		/**
		 * @Before annotation interceptor
		 */
		public static class Before {

			@RuntimeType
			public static void before(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {
				// running server so it can be used even in @Before method
				invokeInitialMethod("beforeTest", orig, false);

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
				invokeInitialMethod("afterTest", orig, false);

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
			public static Object test(@SuperCall Callable<?> zuper, @Origin Method method, @This Testosterone orig) throws Exception {
				boolean isTest = false;

				if (method.isAnnotationPresent(Test.class)) {
					isTest = true;
				}

				try {
					Object obj = null;
//					if (isTest) {
					//TODO: call test method with request but without infinite loop
//						new InvokeRequest(new FrameworkMethod(method), orig).evaluate();
//					} else {
					obj = zuper.call();
//					}
					return obj;
				} catch (Throwable ex) {
					if (ex instanceof AssertionError) {
						TestosteroneMain.getMain(orig).getMain().getMessages().add(ex);
					} else {
						TestosteroneMain.getMain(orig).getMain().getExpectedExceptions().add(ex);
					}
				} finally {
					if (isTest) {
						TestosteroneMain.removeMain(orig);
					}
				}
				return null;
			}
		}

	}

}
