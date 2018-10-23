package info.stasha.testosterone.interceptors;

import info.stasha.testosterone.jersey.JerseyRequestTest;
import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Interceptors
 *
 * @author stasha
 */
public class Intercept {

	/**
	 * Test class interceptor.
	 */
	public static class TestClass {

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
}