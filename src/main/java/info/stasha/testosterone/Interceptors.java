package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Interceptors intercepting JUnit calls to different test instance methods.
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

	/**
	 *
	 * @param methodName
	 * @param orig
	 */
	public static void invokeInitialMethod(String methodName, Testosterone orig) {
		try {
			orig.getClass().getMethod(methodName).invoke(orig);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Interceptors
	 */
	public static class Intercept {

		/**
		 * Constructor interceptor
		 *
		 * @param orig
		 */
		@RuntimeType
		public static void constructor(@This Testosterone orig) {
		}

		/**
		 * @BeforeClass annotation interceptor
		 */
		public static class BeforeClass {

			@RuntimeType
			public static void beforeClass(@Origin Method method) throws Exception {
				beforeClass(method.getDeclaringClass());
			}

			public static void beforeClass(Class<?> clazz) throws Exception {
				
				Setup setup = ConfigFactory.SETUP.get(clazz.getName());
				Testosterone t = setup != null ? setup.getTestosterone() : null;
				t = t == null ? (Testosterone) clazz.newInstance() : t;

				if (t.getSetup().isSuite()
						|| t.getConfiguration().getServerStarts() == Configuration.ServerStarts.PER_CLASS
						|| (t.getSetup().getParent() == null && t.getConfiguration().getServerStarts() == Configuration.ServerStarts.PARENT_CONFIGURATION)) {
					t.start();
				}
			}
		}

		/**
		 * @AfterClass annotation interceptor
		 */
		public static class AfterClass {

			@RuntimeType
			public static void afterClass(@Origin Method method) throws Exception {
				afterClass(method.getDeclaringClass());
			}

			public static void afterClass(Class<?> clazz) throws Exception {
				
				Testosterone t = ConfigFactory.SETUP.get(clazz.getName()).getTestosterone();
				if (t.getSetup().isSuite()
						|| t.getConfiguration().getServerStarts() == Configuration.ServerStarts.PER_CLASS
						|| (t.getSetup().getParent() == null && t.getConfiguration().getServerStarts() == Configuration.ServerStarts.PARENT_CONFIGURATION)) {
					t.getConfiguration().getResourceObject().stop();
				}
			}
		}

		/**
		 * @Before annotation interceptor
		 */
		public static class Before {

			@RuntimeType
			public static void before(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {
				if (orig.getConfiguration().getServerStarts() == Configuration.ServerStarts.PER_TEST) {
					invokeInitialMethod("start", orig);
				}
				zuper.call();
			}

			/**
			 * Invoked by __before__ method
			 *
			 * @param orig
			 * @throws Exception
			 */
			@RuntimeType
			public static void before(@This Testosterone orig) throws Exception {
				if (orig.getConfiguration().getServerStarts() == Configuration.ServerStarts.PER_TEST) {
					invokeInitialMethod("start", orig);
				}
			}
		}

		/**
		 * @After annotation interceptor
		 */
		public static class After {

			@RuntimeType
			public static void after(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {
				if (orig.getConfiguration().getServerStarts() == Configuration.ServerStarts.PER_TEST) {
					invokeInitialMethod("stop", orig);
				}

				zuper.call();
			}

			/**
			 * Invoked by __after__ method
			 *
			 * @param orig
			 * @throws Exception
			 */
			@RuntimeType
			public static void after(@This Testosterone orig) throws Exception {
				if (orig.getConfiguration().getServerStarts() == Configuration.ServerStarts.PER_TEST) {
					invokeInitialMethod("stop", orig);
				}
			}
		}

		/**
		 * @Path and @Test annotation interceptor
		 */
		public static class PathAndTest {

			/**
			 * Method interceptor. This method is always executed as first by
			 * JUnit framework.
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

				ServerConfig config = orig.getConfiguration();
				Testosterone testingObject = config.getTestObject();
				Testosterone resourceObject = config.getResourceObject();

				// This method is always executed as first by JUnit framework.
				// If there is no stored testingObject, we store it.
				if (testingObject == null) {
					config.setTestObject(orig);
					config.setTestThreadName(Thread.currentThread().getName());
					testingObject = orig;
				}

				// Flag if this is the main thread in which JUnit test runs
				boolean isMain = Thread.currentThread().getName().equals(config.getTestThreadName());

				// Jersey resource method that will be invoked
				Method resourceMethod = resourceObject.getClass().getMethod(method.getName(), method.getParameterTypes());

				// If we are in main JUnit thread, invoke http request to test
				if (isMain) {

					try {
						// Invoking http request to resource method on resource object
						resourceObject.getConfigFactory().getTestExecutor(resourceMethod, resourceObject).execute();

						// if any, throw errors produced by test method
						config.throwErrorMessage();

						// if any, throw expection produced by test method
						config.throwExpectedException();

					} finally {
						// clear junit errors
						config.getMessages().clear();

						// clear expected exceptions
						config.getExpectedExceptions().clear();
					}

				} else {
					// if thread is not main JUnit thread, then it is http server thread
					try {

						// Getting original not intercepted @Test or @Path method
						List<Method> me = getMethodsAnnotatedWith(orig.getClass(), resourceMethod.getName() + "$accessor$");
						resourceMethod = me.get(0);
						resourceMethod.setAccessible(true);

						try {
							// executing not intercepted method
							return resourceMethod.invoke(orig, args);
						} catch (IllegalArgumentException ex) {
							System.out.println("Failed to invoke from interceptor");
							ex.printStackTrace();
							throw ex;
						}
					} catch (Throwable ex) {
						if (ex instanceof InvocationTargetException) {
							ex = ex.getCause();
						}

						if (ex instanceof AssertionError) {
							// storing error to messages so they can be thrown in main JUnit thread
							config.getMessages().add(ex);
						} else {
							// storing expected exception so it can be later thrown in main JUnit thread
							config.getExpectedExceptions().add(ex);
						}
					}
				}

				return null;
			}
		}

	}

}
