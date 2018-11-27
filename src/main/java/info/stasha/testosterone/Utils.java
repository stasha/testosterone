package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.annotation.Requests;
import info.stasha.testosterone.db.DbConfig;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author stasha
 */
public class Utils {

    private Utils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Returns class from string
     *
     * @param str
     * @return
     */
    public static Class<?> getClass(String str) {
        try {
            return Class.forName(str);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns new instance of class.
     *
     * @param <T>
     * @param cls
     * @return
     */
    public static <T> T newInstance(Class<?> cls) {
        try {
            return (T) cls.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Reads field value
     *
     * @param <T>
     * @param field
     * @param obj
     * @return
     */
    public static <T> T getFieldValue(Field field, Object obj) {
        try {
            return (T) field.get(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T loadConfig(
            String propertyName,
            Class<T> configInterface,
            Class<?> defaultClass,
            SuperTestosterone testosterone) {

        String systemTestProperty = System.getProperty(propertyName);
        ServiceLoader<T> loader = ServiceLoader.load(configInterface);
        Configuration conf = testosterone.getClass().getAnnotation(Configuration.class);
        T config = null;

        if (conf != null) {
            if (configInterface == TestConfig.class) {
                config = Utils.newInstance(conf.testConfig());
            } else if (configInterface == ServerConfig.class) {
                config = Utils.newInstance(conf.serverConfig());
            } else if (configInterface == DbConfig.class) {
                config = Utils.newInstance(conf.dbConfig());
            }
        } else if (systemTestProperty != null) {
            config = Utils.<T>newInstance(Utils.getClass(systemTestProperty));
        } else if (loader.iterator().hasNext()) {
            config = loader.iterator().next();
        } else {
            config = Utils.newInstance(defaultClass);
        }

        return config;

    }

    /**
     * Returns true/false if passed class is SuperTestosterone class.
     *
     * @param clazz
     * @return
     */
    public static boolean isTestosterone(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return SuperTestosterone.class.isAssignableFrom(clazz);
    }

    /**
     * Returns true/false if passed object is SuperTestosterone object.
     *
     * @param obj
     * @return
     */
    public static boolean isTestosterone(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof SuperTestosterone;
    }

    /**
     * Returns instrumented class name.
     *
     * @param clazz
     * @return
     */
    public static String getInstrumentedClassName(Class<? extends SuperTestosterone> clazz) {
        String name = clazz.getName();
        if (!name.endsWith("_")) {
            return name + "_";
        }
        return name;
    }

    /**
     * Returns instrumented class name.
     *
     * @param obj
     * @return
     */
    public static String getInstrumentedClassName(SuperTestosterone obj) {
        return getInstrumentedClassName(obj.getClass());
    }

    /**
     * Returns testosterone instance based on passed class.
     *
     * @param clazz
     * @return
     */
    public static SuperTestosterone getTestosterone(Class<? extends SuperTestosterone> clazz) {
        SuperTestosterone t = null;
        try {
            TestConfig config = TestConfigFactory.TEST_CONFIGURATIONS.get(getInstrumentedClassName(clazz));
            t = config == null ? (SuperTestosterone) clazz.newInstance() : (SuperTestosterone) config.getTest();
        } catch (Exception ex) {
            LOGGER.error("Failed to initialize new SuperTestosterone object.", ex);
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
        if (clazz == null || annotations == null || annotations.isEmpty()) {
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
     * Returns when server should start.
     *
     * @param clazz
     * @return
     */
    public static StartServer getServerStarts(Class<? extends SuperTestosterone> clazz) {
        TestConfig config = TestConfigFactory.TEST_CONFIGURATIONS.get(getInstrumentedClassName(clazz));

        if (config == null) {
            Configuration cf = clazz.getAnnotation(Configuration.class);
            if (cf != null) {
                return cf.startServer();
            }
            return TestConfig.START_SERVER;
        }

        return config.getStartServer();
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
    public static Object invokeOriginalMethod(Method method, SuperTestosterone target, Object[] data) throws IllegalAccessException, InvocationTargetException {
        Method me = Utils.getOriginalMethod(target.getClass(), method.getName(), method.getParameterTypes());
        try {
            me.setAccessible(true);
            return me.invoke(target, data);
        } catch (Exception ex) {
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
    public static void copyFields(Object source, Object dest) {
        for (Field f : source.getClass().getSuperclass().getDeclaredFields()) {
            try {
                if (f.getName().contains("$") || Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                f.setAccessible(true);
                Field modifiers = f.getClass().getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);

                Object value = Utils.getFieldValue(f, source);
                f.set(dest, value);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
