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
     * __afterClass__ - invoked when JUnit calls @AfterClass.<br>
     * This is just a hack because JUnit4 doesn't fire testRunFinished
     * event.<br>
     * __postconstruct__ - invoked when object is fully prepared including all
     * injections<br>
     * <p>
     * Methods annotated with @Test annotation are also annotated with @Path and
     *
     * @GET annotations. In case @Path annotation is present, then nor @Path nor
     * @GET annotations are added.
     * </p>
     * <p>
     * Methods annotated with @DontIntercept annotation are skipped by
     * instrumentation and will behave as non normal JUnit/Jersey methods.
     * </p>
     *
     * @param clazz
     * @return
     */
    public static Class<? extends Testosterone> testClass(Class<? extends Testosterone> clazz, Annotation afterClassAnnotation) {

        if (!CLASSES.containsKey(clazz)) {

            Class<? extends Testosterone> cls = new ByteBuddy()
                    .subclass(clazz)
                    .name(clazz.getName() + "_")
                    //
                    .defineMethod("__afterClass__", void.class, Visibility.PUBLIC, Ownership.STATIC)
                    .intercept(MethodDelegation.to(Interceptors.Intercept.AfterClass.class))
                    .annotateMethod(afterClassAnnotation)
                    //
                    .constructor(ElementMatchers.isDefaultConstructor())
                    .intercept(SuperMethodCall.INSTANCE)
                    .annotateType(new PathAnnotation(""))
                    //
                    .method(
                            // junit4 annotations
                            isAnnotatedWith(named("org.junit.Test"))
                            .or(isAnnotatedWith(named("org.junit.Before")))
                            .or(isAnnotatedWith(named("org.junit.After")))
                            // junit5 annotations
                            .or(isAnnotatedWith(named("org.junit.jupiter.api.Test")))
                            .or(isAnnotatedWith(named("org.junit.jupiter.api.BeforeEach")))
                            .or(isAnnotatedWith(named("org.junit.jupiter.api.AfterEach")))
                            // testng annotations
                            .or(isAnnotatedWith(named("org.testng.annotations.Test")))
                            .or(isAnnotatedWith(named("org.testng.annotations.Before")))
                            .or(isAnnotatedWith(named("org.testng.annotations.After")))
                            // jax-rs
                            .and(not(isAnnotatedWith(Path.class)))
                            // testosterone
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
        }

        return CLASSES.get(clazz);
    }
}
