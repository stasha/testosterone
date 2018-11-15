package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.annotation.Requests;
import info.stasha.testosterone.jersey.Testosterone;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author stasha
 */
public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Prints class data, methods, arguments, annotations ...
     *
     * @param clazz
     */
    public static void printClassData(Class<?> clazz) {
        System.out.println("----");
        System.out.println("Class data for: " + clazz.getName());
        for (final Method method : clazz.getDeclaredMethods()) {
            for (Annotation anon : method.getAnnotations()) {
                System.out.println(anon.annotationType().getName());
                for (Field field : anon.getClass().getDeclaredFields()) {
//                    System.out.println("   " + field.getName());
                }
            }
            System.out.println(method.getName());
            for (Parameter param : method.getParameters()) {
                System.out.println("  param: " + param.getName());
                for (Annotation a : param.getAnnotations()) {
                    System.out.println("      " + a.annotationType().getName());
                }
            }

        }
    }

    /**
     * Returns testosterone instance based on passed class.
     *
     * @param clazz
     * @return
     */
    public static Testosterone getTestosterone(Class<? extends Testosterone> clazz) {
        Testosterone t = null;
        try {
            Setup setup = ConfigFactory.SETUP.get(clazz.getName());
            t = setup != null ? setup.getTestosterone() : null;
            t = t == null ? (Testosterone) clazz.newInstance() : t;
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.error("Failed to initialize new Testosterone object.", ex);
            throw new RuntimeException(ex);
        }
        return t;
    }

    /**
     * Returns all methods that are annotated with specified annotation.
     *
     * @param clazz
     * @param annotations
     * @return
     */
    public static List<Method> getAnnotatedMethods(Class<?> clazz, List<Class<? extends Annotation>> annotations) {
        List<Method> methods = new ArrayList<>();
        if (clazz == null || annotations.isEmpty()) {
            return methods;
        }
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                for (Class<? extends Annotation> a : annotations) {
                    if (method.isAnnotationPresent(a)) {
                        methods.add(method);
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    /**
     * Returns Annotation from class, super class or implemented interfaces.
     *
     * @param <T>
     * @param o
     * @param annotation
     * @return
     */
    public static <T> T getAnnotation(Object o, Class<? extends Annotation> annotation) {
        return getAnnotation(o.getClass(), annotation);
    }

    /**
     * Returns annotation from method.
     *
     * @param <T>
     * @param m
     * @param annotation
     * @param includingSuper
     * @return
     */
    public static <T> T getAnnotation(Method m, Class<? extends Annotation> annotation, boolean includingSuper) {
        Annotation a = m.getAnnotation(annotation);

        if (a != null) {
            return (T) a;
        }

        if (includingSuper) {
            Class<?> clazz = m.getDeclaringClass().getSuperclass();

            while (clazz != null && m != null) {
                try {
                    m = clazz.getMethod(m.getName(), m.getParameterTypes());
                    if (m != null) {
                        return getAnnotation(m, annotation, includingSuper);
                    }
                } catch (NoSuchMethodException | SecurityException ex) {
//                    java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
                }

                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }

    /**
     * Returns Annotation from class, super class or implemented interfaces.
     *
     * @param <T>
     * @param clazz
     * @param annotation
     * @return
     */
    public static <T> T getAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {

        while (clazz != null) {
            for (Annotation a : clazz.getDeclaredAnnotations()) {
                if (a.annotationType().equals(annotation)) {
                    return (T) a;
                }
            }

            for (Class<?> interfaze : clazz.getInterfaces()) {
                for (Annotation a : interfaze.getDeclaredAnnotations()) {
                    if (a.annotationType().equals(annotation)) {
                        return (T) a;
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * Returns true/false if annotation is present on specified object.
     *
     * @param o
     * @param annotation
     * @return
     */
    public static boolean isAnnotationPresent(Object o, Class<? extends Annotation> annotation) {
        return getAnnotation(o, annotation) != null;
    }

    /**
     * Returns true/false if annotation is present on specified class
     *
     * @param clazz
     * @param annotation
     * @return
     */
    public static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotation) {
        return getAnnotation(clazz, annotation) != null;
    }

    /**
     * Returns when server should start.
     *
     * @param clazz
     * @return
     */
    public static Start getServerStarts(Class<? extends Testosterone> clazz) {
        Setup setup = ConfigFactory.SETUP.get(clazz.getName());
        Testosterone t = setup != null ? setup.getTestosterone() : null;

        if (t == null) {
            Configuration config = clazz.getAnnotation(Configuration.class
            );
            if (config != null) {
                return config.serverStarts();
            }
        }
        return Start.BY_PARENT;
    }

    /**
     * Returns original not intercepted method.
     *
     * @param clazz
     * @param methodName
     * @param types
     * @return
     */
    private static Method getOriginalMethod(Class<?> clazz, String methodName, Class<?>[] types) {
        return getMethodStartingWithName(clazz, methodName + "$accessor$", types);
    }

    /**
     * Invokes original not intercepted method.
     *
     * @param method
     * @param target
     * @param data
     * @return
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static Object invokeOriginalMethod(Method method, Testosterone target, Object[] data) throws IllegalAccessException, InvocationTargetException {
        Method me = Utils.getOriginalMethod(target.getClass(), method.getName(), method.getParameterTypes());
        try {
            me.setAccessible(true);
            return me.invoke(target, data);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Failed to invoke method " + me.getName(), ex);
            throw ex;
        }
    }

    /**
     * Returns methos starting with passed method name.
     *
     * @param clazz
     * @param methodNameStartingWith
     * @return
     */
    public static Method getMethodStartingWithName(Class<?> clazz, Method methodNameStartingWith) {
        return getMethodStartingWithName(clazz, methodNameStartingWith.getName(), methodNameStartingWith.getParameterTypes());
    }

    /**
     * Returns method starting with specified name.
     *
     * @param clazz
     * @param methodNameStartingWith
     * @param params
     * @return
     */
    public static Method getMethodStartingWithName(Class<?> clazz, String methodNameStartingWith, Class<?>[] params) {
        for (Method m : getMethodsStartingWithName(clazz, methodNameStartingWith)) {
            if (Arrays.equals(m.getParameterTypes(), params)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Returns list of methods that are starting with specified name.
     *
     * @param clazz
     * @param methodNameStartingWith
     * @return
     */
    public static List<Method> getMethodsStartingWithName(Class<?> clazz, String methodNameStartingWith) {
        final List<Method> methods = new ArrayList<>();
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith(methodNameStartingWith)) {
                methods.add(method);
            }
        }
        return methods;
    }

    /**
     * Returns true/false if method has @Requests or @Request annotation
     *
     * @param method
     * @return
     */
    public static boolean hasRequestAnnotation(Method method) {
        return method.isAnnotationPresent(Requests.class) || method.isAnnotationPresent(Request.class);
    }

    /**
     * Copies all fields from source object to destination object.
     *
     * @param source
     * @param dest
     */
    public static void copyFields(Object source, Object dest) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        for (Field f : source.getClass().getSuperclass().getDeclaredFields()) {
            if (f.getName().contains("$") || Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            f.setAccessible(true);
            Field modifiers = f.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);

            Object value = f.get(source);
            f.set(dest, value);
        }
    }
}
