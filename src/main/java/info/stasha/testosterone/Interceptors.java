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
		}

		/**
		 * @BeforeClass annotation interceptor
		 */
		public static class BeforeClass {

			@RuntimeType
			public static void beforeClass(@Origin Method method) throws Exception {
				TestosteroneSetup setup = TestosteroneConfigFactory.SETUP.get(method.getDeclaringClass().getName());
				Testosterone t = setup != null ? setup.getTestosterone() : null;
				t = t == null ? (Testosterone) method.getDeclaringClass().newInstance() : t;

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
				Testosterone t = TestosteroneConfigFactory.SETUP.get(method.getDeclaringClass().getName()).getTestosterone();
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
					System.out.println("__before__");
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
					System.out.println("__after__");
					invokeInitialMethod("stop", orig);
				}
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

				TestosteroneConfig config = orig.getConfiguration();
				Testosterone testingObject = config.getTestObject();
				Testosterone resourceObject = config.getResourceObject();

				if (testingObject == null) {
					config.setTestObject(orig);
					config.setTestThreadName(Thread.currentThread().getName());
					testingObject = orig;
				}

				boolean isMain = Thread.currentThread().getName().equals(config.getTestThreadName());
				Method resourceMethod = resourceObject.getClass().getMethod(method.getName(), method.getParameterTypes());

				if (isMain) {

					try {
						new InvokeTest(resourceMethod, resourceObject).execute();

						config.throwErrorMessage();
						config.throwExpectedException();

					} finally {
						config.getMessages().clear();
						config.getExpectedExceptions().clear();
					}

				} else {
					try {
						List<Method> me = getMethodsAnnotatedWith(orig.getClass(), resourceMethod.getName() + "$accessor$");

						resourceMethod = me.get(0);
						resourceMethod.setAccessible(true);

						try {
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
							config.getMessages().add(ex);
						} else {
//							ex.printStackTrace();
							config.getExpectedExceptions().add(ex);
						}
					}
				}

				return null;
			}
		}

	}

}
