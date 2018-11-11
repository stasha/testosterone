package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptors intercepting JUnit calls to different test instance methods.
 *
 * @author stasha
 */
public class Interceptors {

    private static final Logger LOGGER = LoggerFactory.getLogger(Interceptors.class);

    /**
     * Starts the configuration.
     *
     * @param t
     */
    private static void start(Testosterone t) {
        try {
            t.start();
        } catch (Exception ex) {
            LOGGER.error("Failed to start configuration.", ex);
            try {
                t.stop();
            } catch (Exception ex1) {
                LOGGER.error("Failed to stop configuration.", ex);
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(ex);
        }
    }

    /**
     * Stops the configuration.
     *
     * @param t
     */
    private static void stop(Testosterone t) {
        try {
            t.stop();
        } catch (Exception ex) {
            LOGGER.error("Failed to stop configuration.", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Start configuration before test class.
     *
     * @param clazz
     */
    public static void beforeClass(Class<? extends Testosterone> clazz) {
        StartServer s = Utils.getServerStarts(clazz);
        if (s == StartServer.BY_PARENT || s == StartServer.PER_CLASS) {
            LOGGER.info("Starting server configured with: {}", s);
            start(Utils.getTestosterone(clazz));
        }
    }

    /**
     * Stop configuration after test class.
     *
     * @param clazz
     */
    public static void afterClass(Class<? extends Testosterone> clazz) {
        StartServer s = Utils.getServerStarts(clazz);
        if (s == StartServer.BY_PARENT || s == StartServer.PER_CLASS) {
            LOGGER.info("Stopping server configured with: {}", s);
            stop(Utils.getTestosterone(clazz));
        }
    }

    /**
     * Start configuration before test method.
     *
     * @param clazz
     */
    public static void before(Class<? extends Testosterone> clazz) {
        before(Utils.getTestosterone(clazz));
    }

    /**
     * Start configuration before test method.
     *
     * @param orig
     */
    public static void before(@This Testosterone orig) {
        if (orig.getServerConfig().getServerStarts() == StartServer.PER_TEST) {
            LOGGER.info("Starting server {} configuration", orig.getServerConfig().getServerStarts());
            start(orig);
        }
    }

    /**
     * Stop configuration after test method.
     *
     * @param clazz
     */
    public static void after(Class<? extends Testosterone> clazz) {
        after(Utils.getTestosterone(clazz));
    }

    /**
     * Stop configuration after test method.
     *
     * @param orig
     */
    public static void after(@This Testosterone orig) {
        if (orig.getServerConfig().getServerStarts() == StartServer.PER_TEST) {
            LOGGER.info("Stopping server {} configuration", orig.getServerConfig().getServerStarts());
            stop(orig);
        }
    }

    /**
     * Interceptors
     */
    public static class Intercept {

        /**
         * @PostConstruct interceptor
         */
        public static class PostConstruct {

            @RuntimeType
            public static void postConstruct(@This Testosterone orig) throws Exception {
                LOGGER.info("Invoking @PostConstruct");
                orig.getSetup().afterServerStart(orig);
            }
        }

        /**
         * @AfterClass annotation interceptor
         */
        public static class AfterClass {

            @RuntimeType
            public static void afterClass(@Origin Method method) throws Exception {
                Interceptors.afterClass((Class<? extends Testosterone>) method.getDeclaringClass());
            }
        }

        /**
         * @Path and @Test annotation interceptor
         */
        public static class PathAndTest {

            /**
             * Method interceptor. This before is always executed as first by
             * JUnit framework.
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

                String invoking = orig.getClass().getName() + ":" + method.getName();

                ServerConfig config = orig.getServerConfig();
                Testosterone testingObject = config.getTestObject();
                Testosterone resourceObject = config.getResourceObject();

                // This before is always executed as first by JUnit framework.
                // If there is no stored testingObject, we store it.
                if (testingObject == null) {
                    config.setTestObject(orig);
                    config.setTestThreadName(Thread.currentThread().getName());
                }

                // Flag if this is the main thread in which JUnit test runs
                boolean isMain = Thread.currentThread().getName().equals(config.getTestThreadName());

                // Jersey resource before that will be invoked
                Method resourceMethod = resourceObject.getClass().getMethod(method.getName(), method.getParameterTypes());

                // If we are in main JUnit thread, invoke http request to test
                if (isMain) {
                    LOGGER.info("Starting {}", invoking);

                    try {
                        try {
                            // Invoking http request to resource before on resource object
                            resourceObject.getConfigFactory().getTestExecutor(resourceMethod, resourceObject).execute();

                        } catch (InvocationTargetException ex) {
                            if (ex.getCause() instanceof AssertionError) {
                                throw ex.getCause();
                            }
                        } catch (Exception ex) {
                            LOGGER.error("Failed to execute test " + invoking, ex);
                        }
                        // if any, throw errors produced by test before
                        config.throwExceptions();

                    } finally {
                        // clear junit errors
                        config.getExceptions().clear();
                        LOGGER.info("Ending test {}", invoking);
                    }

                } else {
                    // if thread is not main JUnit thread, then it is http server thread
                    try {
                        LOGGER.info("Invoking {}", invoking);
                        return Utils.invokeOriginalMethod(resourceMethod, orig, args);
                    } catch (Throwable ex) {
                        if (ex instanceof InvocationTargetException) {
                            ex = ex.getCause();
                        }

                        // storing error to messages so they can be thrown in main JUnit thread
                        config.getExceptions().add(ex);

                    }
                }

                return null;
            }
        }

    }

}
