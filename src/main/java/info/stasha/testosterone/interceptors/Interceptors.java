package info.stasha.testosterone.interceptors;

import info.stasha.testosterone.jerseyon.TestosteroneMain;
import info.stasha.testosterone.jerseyon.Testosterone;
import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Interceptors
 *
 * @author stasha
 */
public class Interceptors {

	/**
	 * Test class interceptors.
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
//				System.out.println("before");
				zuper.call();
			}
		}

		/**
		 * @After annotation interceptor
		 */
		public static class After {

			@RuntimeType
			public static void after(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {

				try {
					zuper.call();
				} catch (AssertionError error) {
					System.out.println(error);
				}

			}
		}

		/**
		 * @After annotation interceptor
		 */
		public static class Test {

			/**
			 * Method interceptor
			 *
			 * @param zuper
			 * @param orig
			 * @return
			 * @throws Exception
			 */
			@RuntimeType
//			public static Object test(@Origin Method zuper, @This Testosterone orig) throws Exception {
			public static Object test(@SuperCall Callable<?> zuper, @This Testosterone orig) throws Exception {
				try {
					Object obj = zuper.call();
					return obj;
				} catch (Throwable ex) {
					if (ex instanceof AssertionError) {
						TestosteroneMain.getMain(orig).getMain().getMessages().add(ex);
					} else {
						TestosteroneMain.getMain(orig).getMain().getExpectedExceptions().add(ex);
					}
				} finally {
					TestosteroneMain.removeMain(orig);
				}
				return null;
			}
		}

	}

}
