package info.stasha.testosterone.instrumentation;

import info.stasha.testosterone.annotation.DontIntercept;
import info.stasha.testosterone.annotation.GetAnnotation;
import info.stasha.testosterone.annotation.PathAnnotation;
import info.stasha.testosterone.interceptors.Intercept;
import javax.ws.rs.Path;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import static net.bytebuddy.matcher.ElementMatchers.anyOf;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.not;
import org.junit.Test;

/**
 * Instrumentation
 *
 * @author stasha
 */
public class Instrument {

	/**
	 * Returns new class that extends test class
	 *
	 * @param clazz
	 * @return
	 */
	public static Class<?> testClass(Class<?> clazz) {

		return new ByteBuddy()
				.subclass(clazz)
				.name(clazz.getName() + "_")
				.method(isAnnotatedWith(anyOf(Test.class))
						.and(not(isAnnotatedWith(Path.class)))
						.and(not(isAnnotatedWith(DontIntercept.class)))
				)
				.intercept(MethodDelegation.to(Intercept.TestClass.class))
				.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
				.annotateMethod(new PathAnnotation())
				.annotateMethod(new GetAnnotation())
				.method(isAnnotatedWith(Path.class)
						.and(not(isAnnotatedWith(DontIntercept.class)))
				)
				.intercept(MethodDelegation.to(Intercept.TestClass.class))
				.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
				.make()
				.load(clazz.getClassLoader())
				.getLoaded();

	}
}
