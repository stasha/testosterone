package info.stasha.testosterone;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

import javax.ws.rs.Path;

import info.stasha.testosterone.annotation.DontIntercept;
import info.stasha.testosterone.jersey.GetAnnotation;
import info.stasha.testosterone.jersey.PathAnnotation;
import info.stasha.testosterone.jersey.PostConstructAnnotation;
import info.stasha.testosterone.jersey.Testosterone;
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
 * Instrumentation.
 *
 * @author stasha
 */
public class Instrument {

	private static final Map<Class<? extends Testosterone>, Class<? extends Testosterone>> CLASSES = new HashMap<>();

	/**
	 * Returns already instrumented class.
	 *
	 * @param clazz
	 * @return
	 */
	public static Class<? extends Testosterone> getInstrumentedClass(Class<? extends Testosterone> clazz) {
		return CLASSES.get(clazz);
	}

	/**
	 * Returns new instrumented class that extends test class. It adds
	 * methods:<br>
	 * __created__ - invoked when test class is created<br>
	 * __afterClass__ - invoked when JUnit calls @AfterClass. 
	 * This is just a hack because JUnit4 doesn't fire testRunFinished event.<br>
	 *<p>
	 * Methods annotated with @Test annotation are also annotated with @Path and
	 * @GET annotations. In case @Path annotation is present, then nor @Path nor
	 * @GET annotations are added.
	 * </p>
	 * <p>
	 * Methods annotated with @DontIntercept annotation are skipped by
	 * instrumentation and will behave as non normal JUnit/Jersey methods.
	 * </p>
	 * <p>
	 * All methods annotated with @BeforeClass, @AfterClass, @Before, @After,
	 * @Test and @Path are intercepted.
	 * </p>
	 *
	 *
	 * @param clazz
	 * @return
	 */
	public static Class<? extends Testosterone> testClass(Class<? extends Testosterone> clazz, Annotation afterClassAnnotation) {

		if (!Testosterone.class.isAssignableFrom(clazz)) {
			return clazz;
		}

		if (!CLASSES.containsKey(clazz)) {

			Class<? extends Testosterone> cls = new ByteBuddy()
					.subclass(clazz)
					.name(clazz.getName() + "_")
					//
					.defineMethod("__created__", void.class, Visibility.PUBLIC)
					.intercept(MethodDelegation.to(Interceptors.Intercept.class))
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
					.method(isAnnotatedWith(named("org.junit.Test"))
							.or(isAnnotatedWith(named("org.junit.jupiter.api.Test")))
							.or(isAnnotatedWith(named("org.testng.annotations.Test")))
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
					.defineMethod("__postconstruct__", void.class, Visibility.PUBLIC)
					.intercept(MethodDelegation.to(Interceptors.Intercept.PostConstruct.class))
					.annotateMethod(new PostConstructAnnotation())
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
