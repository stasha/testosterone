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

	public static List<Method> getMethodsAnnotatedWith(final Class<? extends Testosterone> type, final String className) {
		final List<Method> methods = new ArrayList<>();
		Class<? extends Testosterone> klass = type;
		final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
		for (final Method method : allMethods) {
			if (method.getName().startsWith(className)) {
				methods.add(method);
			}
//			System.out.println(method.getName());
//			for(Parameter param : method.getParameters()){
//				System.out.println("  param: " + param.getName());
//				for(Annotation a : param.getAnnotations()){
//					System.out.println("      " + a.annotationType().getName());
//				}
//			}
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
		 * @PostConstruct interceptor
		 */
		public static class PostConstruct {

			@RuntimeType
			public static void postConstruct(@This Testosterone orig) throws Exception {
				orig.getSetup().afterServerStart(orig);
			}
		}

		public static Testosterone getTestosterone(Class<? extends Testosterone> clazz) {
			Testosterone t = null;
			try {
				Setup setup = ConfigFactory.SETUP.get(clazz.getName());
				t = setup != null ? setup.getTestosterone() : null;
				t = t == null ? (Testosterone) clazz.newInstance() : t;
			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex);
			}
			return t;
		}

		/**
		 * @BeforeClass annotation interceptor
		 */
		public static class BeforeClass {

			@RuntimeType
			public static void beforeClass(@Origin Method method) throws Exception {
				beforeClass((Class<? extends Testosterone>) method.getDeclaringClass());
			}

			public static void beforeClass(Class<? extends Testosterone> clazz) {
				Testosterone t = getTestosterone(clazz);
				if (t.getSetup().isSuite()
						|| t.getServerConfig().getServerStarts() == Configuration.ServerStarts.PER_CLASS
						|| (t.getSetup().getParent() == null && t.getServerConfig().getServerStarts() == Configuration.ServerStarts.PARENT_CONFIGURATION)) {
					try {
						t.start();
					} catch (Exception ex) {
						try {
							t.stop();
						} catch (Exception ex1) {
							Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex1);
						}
						Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}

		/**
		 * @AfterClass annotation interceptor
		 */
		public static class AfterClass {

			@RuntimeType
			public static void afterClass(@Origin Method method) throws Exception {
				afterClass((Class<? extends Testosterone>) method.getDeclaringClass());
			}

			public static void afterClass(Class<? extends Testosterone> clazz) {

				Testosterone t = getTestosterone(clazz);
				if (t.getSetup().isSuite()
						|| t.getServerConfig().getServerStarts() == Configuration.ServerStarts.PER_CLASS
						|| (t.getSetup().getParent() == null && t.getServerConfig().getServerStarts() == Configuration.ServerStarts.PARENT_CONFIGURATION)) {
					try {
						t.stop();
					} catch (Exception ex) {
						Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}

		/**
		 * @Before annotation interceptor
		 */
		public static class Before {

			/**
			 * Invoked by __before__ method
			 *
			 * @param orig
			 * @throws Exception
			 */
			@RuntimeType
			public static void before(@This Testosterone orig) {
				if (orig.getServerConfig().getServerStarts() == Configuration.ServerStarts.PER_TEST) {
					try {
						orig.start();
					} catch (Exception ex) {
						try {
							orig.stop();
						} catch (Exception ex1) {
							Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex1);
						}
						Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}

			@RuntimeType
			public static void before(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {
				before(orig);

				zuper.call();
			}

			public static void before(Class<? extends Testosterone> clazz) {
				before(getTestosterone(clazz));
			}
		}

		/**
		 * @After annotation interceptor
		 */
		public static class After {

			/**
			 * Invoked by __after__ method
			 *
			 * @param orig
			 * @throws Exception
			 */
			@RuntimeType
			public static void after(@This Testosterone orig) {
				if (orig.getServerConfig().getServerStarts() == Configuration.ServerStarts.PER_TEST) {
					try {
						orig.stop();
					} catch (Exception ex) {
						Logger.getLogger(Interceptors.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}

			@RuntimeType
			public static void after(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {
				after(orig);

				zuper.call();
			}

			public static void after(Class<? extends Testosterone> clazz) {
				after(getTestosterone(clazz));
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

				ServerConfig config = orig.getServerConfig();
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
						config.throwExceptions();

					} finally {
						// clear junit errors
						config.getExceptions().clear();
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

						// storing error to messages so they can be thrown in main JUnit thread
						config.getExceptions().add(ex);

					}
				}

				return null;
			}
		}

	}

}
