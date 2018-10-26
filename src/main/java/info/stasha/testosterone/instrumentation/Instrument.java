package info.stasha.testosterone.instrumentation;

import info.stasha.testosterone.annotation.DontIntercept;
import info.stasha.testosterone.annotation.GetAnnotation;
import info.stasha.testosterone.annotation.PathAnnotation;
import info.stasha.testosterone.interceptors.Interceptors;
import javax.ws.rs.Path;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatchers;
import static net.bytebuddy.matcher.ElementMatchers.anyOf;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Instrumentation
 *
 * @author stasha
 */
public class Instrument {

	/**
	 * Returns new instrumented class that extends test class
	 *
	 * @param clazz
	 * @return
	 */
	public static Class<?> testClass(Class<?> clazz) throws NoSuchMethodException {

		return new ByteBuddy()
				.subclass(clazz)
				.name(clazz.getName() + "_")
				.defineMethod("__created__", Void.class, Visibility.PUBLIC)
				.intercept(MethodDelegation.to(Interceptors.Intercept.class))
				.constructor(ElementMatchers.isDefaultConstructor())
				.intercept(SuperMethodCall.INSTANCE.andThen(
						MethodCall.invoke(named("__created__"))))
				.annotateType(new PathAnnotation(""))
				
				.method(isAnnotatedWith(Before.class))
				.intercept(MethodDelegation.to(Interceptors.Intercept.Before.class))
				.attribute(MethodAttributeAppender.NoOp.INSTANCE)
				
				.method(isAnnotatedWith(After.class))
				.intercept(MethodDelegation.to(Interceptors.Intercept.After.class))
				.attribute(MethodAttributeAppender.NoOp.INSTANCE)
				
				.method(isAnnotatedWith(Test.class)
						.and(not(isAnnotatedWith(Path.class)))
						.and(not(isAnnotatedWith(DontIntercept.class)))
				)
				.intercept(MethodDelegation.to(Interceptors.Intercept.Test.class))
				.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
				.annotateMethod(new PathAnnotation())
				.annotateMethod(new GetAnnotation())
				
				.method(isAnnotatedWith(Path.class)
						.and(not(isAnnotatedWith(DontIntercept.class)))
				)
				.intercept(MethodDelegation.to(Interceptors.Intercept.Test.class))
				.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
				
				
				.make()
				.load(clazz.getClassLoader())
				.getLoaded();

	}
}
