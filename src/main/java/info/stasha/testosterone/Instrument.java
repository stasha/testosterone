package info.stasha.testosterone;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

import javax.ws.rs.Path;

import info.stasha.testosterone.annotation.DontIntercept;
import info.stasha.testosterone.jersey.GetAnnotation;
import info.stasha.testosterone.jersey.PathAnnotation;
import info.stasha.testosterone.junit4.AfterAnnotation;
import info.stasha.testosterone.junit4.BeforeAnnotation;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Instrumentation
 *
 * @author stasha
 */
public class Instrument {

	private static final Map<Class<?>, Class<?>> CLASSES = new HashMap<>();

	/**
	 * Returns new instrumented class that extends test class
	 *
	 * @param clazz
	 * @param beforeAnnotation
	 * @param afterAnnotation
	 * @param beforeClassAnnotation
	 * @param afterClassAnnotation
	 * @return
	 */
	public static Class<?> testClass(
			Class<?> clazz,
			Annotation beforeAnnotation,
			Annotation afterAnnotation,
			Annotation beforeClassAnnotation, 
			Annotation afterClassAnnotation) {

		if (!CLASSES.containsKey(clazz)) {

			Class<?> cls = new ByteBuddy()
					.subclass(clazz)
					.name(clazz.getName() + "_")
					//
					.defineMethod("__created__", void.class, Visibility.PUBLIC)
					.intercept(MethodDelegation.to(Interceptors.Intercept.class))
					//
					.defineMethod("__beforeClass__", void.class, Visibility.PUBLIC, Ownership.STATIC)
					.intercept(MethodDelegation.to(Interceptors.Intercept.BeforeClass.class))
					.annotateMethod(beforeClassAnnotation)
					//
					.defineMethod("__afterClass__", void.class, Visibility.PUBLIC, Ownership.STATIC)
					.intercept(MethodDelegation.to(Interceptors.Intercept.AfterClass.class))
					.annotateMethod(afterClassAnnotation)
					//
					.constructor(ElementMatchers.isDefaultConstructor())
					.intercept(SuperMethodCall.INSTANCE.andThen(
							MethodCall.invoke(named("__created__"))))
					.annotateType(new PathAnnotation(""))
					//
					.method(isAnnotatedWith(named("org.junit.Before"))
							.or(isAnnotatedWith(named("org.junit.jupiter.api.BeforeEach"))))
					.intercept(MethodDelegation.to(Interceptors.Intercept.Before.class))
					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
//					.attribute(MethodAttributeAppender.NoOp.INSTANCE)
					//
					.method(isAnnotatedWith(named("org.junit.After"))
							.or(isAnnotatedWith(named("org.junit.jupiter.api.AfterEach"))))
					.intercept(MethodDelegation.to(Interceptors.Intercept.After.class))
					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
//					.attribute(MethodAttributeAppender.NoOp.INSTANCE)
					//
					.method(isAnnotatedWith(named("org.junit.Test"))
							.or(isAnnotatedWith(named("org.junit.jupiter.api.Test")))
							.and(not(isAnnotatedWith(Path.class)))
							.and(not(isAnnotatedWith(DontIntercept.class)))
					)
					.intercept(MethodDelegation.to(Interceptors.Intercept.PathAndTest.class))
					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
					.annotateMethod(new PathAnnotation())
					.annotateMethod(new GetAnnotation())
					//
					.method(isAnnotatedWith(Path.class)
							.and(not(isAnnotatedWith(DontIntercept.class)))
					)
					.intercept(MethodDelegation.to(Interceptors.Intercept.PathAndTest.class))
					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
					//
					.defineMethod("__before__", void.class, Visibility.PUBLIC)
					.intercept(MethodDelegation.to(Interceptors.Intercept.Before.class))
//					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
					.attribute(MethodAttributeAppender.NoOp.INSTANCE)
					.annotateMethod(beforeAnnotation)
					//
					.defineMethod("__after__", void.class, Visibility.PUBLIC)
					.intercept(MethodDelegation.to(Interceptors.Intercept.After.class))
//					.attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
					.attribute(MethodAttributeAppender.NoOp.INSTANCE)
					.annotateMethod(afterAnnotation)
					//
					.make()
					.load(clazz.getClassLoader())
					.getLoaded();

			CLASSES.put(clazz, cls);

//					Interceptors.getMethodsAnnotatedWith(cls, "Test");
		}

		return CLASSES.get(clazz);
	}
}
