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
import org.glassfish.hk2.api.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestInterceptors intercepting calls to test methods.
 *
 * @author stasha
 */
public class TestInterceptors {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestInterceptors.class);

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
        Start s = Utils.getServerStarts(clazz);
        if (s == Start.BY_PARENT || s == Start.PER_CLASS) {
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
        Start s = Utils.getServerStarts(clazz);
        if (s == Start.BY_PARENT || s == Start.PER_CLASS) {
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
        if (orig.getServerConfig().getServerStarts() == Start.PER_TEST) {
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
        if (orig.getServerConfig().getServerStarts() == Start.PER_TEST) {
            LOGGER.info("Stopping server {} configuration", orig.getServerConfig().getServerStarts());
            stop(orig);
        }
    }

    /**
     * TestInterceptors
     */
    public static class Intercept {

        public static class Constructor {

            /**
             * Constructor interceptor
             *
             * @param orig
             * @throws java.lang.IllegalAccessException
             * @throws java.lang.reflect.InvocationTargetException
             * @throws java.lang.NoSuchFieldException
             */
            @RuntimeType
            public static void constructor(@This Testosterone orig) throws IllegalAccessException, InvocationTargetException, IllegalArgumentException, NoSuchFieldException {
                Setup setup = orig.getSetup();
                ServerConfig sc = orig.getServerConfig();
                if (sc.getTestThreadName() == null || Thread.currentThread().getName().equals(sc.getTestThreadName())) {
//                    System.out.println("test thread");
                } else {
                    Testosterone test = sc.getMainThreadTestObject();
                    Utils.copyFields(test, orig);
                }
            }
        }

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
         * @PostConstruct interceptor
         */
        public static class GenericTest {

            @RuntimeType
            public static Void postConstruct(@This Testosterone orig) throws Exception, Throwable {
                TestInExecution et = orig.getSetup().getExecutingTest();
                PathAndTest.test(null, null, et.getMainThreadTestMethod(), et.getManiThreadTest());
                return null;
            }
        }

        public static class Before {

            /**
             *
             * @param zuper
             * @param method
             * @param orig
             */
            public static void before(@SuperCall Callable<?> zuper, @Origin Method method, @This Testosterone orig) {
                // do nothing
                // it's called later manually in http request scope
            }
        }

        public static class After {

            /**
             *
             * @param zuper
             * @param method
             * @param orig
             */
            public static void after(@SuperCall Callable<?> zuper, @Origin Method method, @This Testosterone orig) {
                // do nothing
                // it's called later manually in http request scope
            }
        }

        /**
         * @AfterClass annotation interceptor
         */
        public static class AfterClass {

            @RuntimeType
            public static void afterClass(@Origin Method method) throws Exception {
                TestInterceptors.afterClass((Class<? extends Testosterone>) method.getDeclaringClass());
            }
        }

        /**
         * @BeforeClass annotation interceptor
         */
        public static class BeforeClass {

            @RuntimeType
            public static void beforeClass(@Origin Method method) throws Exception {
                TestInterceptors.beforeClass((Class<? extends Testosterone>) method.getDeclaringClass());
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
                ServiceLocator locator = orig.getSetup().getServiceLocator();
                ServerConfig config = orig.getServerConfig();
                Testosterone mainThreadTest = config.getMainThreadTestObject();
                Testosterone requestThreadTest = config.getRequestThreadTestObject();

                // This before is always executed as first by JUnit framework.
                // If there is no stored testingObject, we store it.
                if (requestThreadTest == null) {
                    config.setRequestThreadTestObject(orig);
                    config.setTestThreadName(Thread.currentThread().getName());
                }

                Utils.copyFields(mainThreadTest, orig);
                // Flag if this is the main thread in which JUnit test runs
                boolean isMain = Thread.currentThread().getName().equals(config.getTestThreadName());

                // Jersey resource before that will be invoked
                Method resourceMethod = mainThreadTest.getClass().getMethod(method.getName(), method.getParameterTypes());

                TestInExecution et = orig.getSetup().getExecutingTest();
                // If we are in main JUnit thread, invoke http request to test
                if (isMain) {
                    LOGGER.info("Starting {}", invoking);

                    try {
                        try {
                            et = new TestInExecution(orig, mainThreadTest, resourceMethod, Utils.getMethodStartingWithName(mainThreadTest.getClass(), resourceMethod), args);
                            orig.getSetup().setExecutingTest(et);
                            // Invoking http request to resource before on resource object
                            et.executeTest();
                        } catch (InvocationTargetException ex) {
                            throw ex.getCause();
                        } catch (Exception ex) {
                            LOGGER.error("Failed to execute test " + invoking, ex);
                            throw ex.getCause();
                        }
                        // if any, throw errors produced by test before
                        config.throwExceptions();

                    } finally {
                        // clear junit errors
                        config.getExceptions().clear();
                        orig.getSetup().setRequestsAlreadInvoked(false);
                        LOGGER.info("Ending test {}", invoking);
                    }

                } else {
                    // if thread is not main JUnit thread, then it is http servers thread
                    locator.inject(orig);

                    try {
                        LOGGER.info("Invoking {}", invoking);
                        for (Method m : Utils.getAnnotatedMethods(orig.getClass(), TestAnnotations.BEFORE)) {
                            Utils.invokeOriginalMethod(m, orig, new Object[]{});
                        }

                        if (Utils.hasRequestAnnotation(method) && !orig.getSetup().isRequestsAlreadInvoked()) {
                            orig.getSetup().setRequestsAlreadInvoked(true);
                            et.executeRequests();
                        } else {
//                            Utils.copyFields(mainThreadTest, orig);
                            return Utils.invokeOriginalMethod(resourceMethod, orig, args);
                        }
                    } catch (Throwable ex) {
                        if (ex instanceof InvocationTargetException) {
                            ex = ex.getCause();
                        }

                        // storing error to messages so they can be thrown in main JUnit thread
                        config.getExceptions().add(ex);

                    } finally {
                        for (Method m : Utils.getAnnotatedMethods(orig.getClass(), TestAnnotations.AFTER)) {
                            Utils.invokeOriginalMethod(m, orig, new Object[]{});
                        }
                    }
                }

                return null;
            }
        }

    }

}
