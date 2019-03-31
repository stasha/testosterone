package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Request;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.Callable;
import javax.ws.rs.Path;
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

    private TestInterceptors() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TestInterceptors.class);

    /**
     * Starts the configuration.
     *
     * @param t
     */
    private static void start(SuperTestosterone t) {
        try {
            t.getTestConfig().start();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Stops the configuration.
     *
     * @param t
     */
    private static void stop(SuperTestosterone t) {
        try {
            t.getTestConfig().stop();
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    /**
     * StartServer configuration before test class.
     *
     * @param clazz
     */
    public static void beforeClass(Class<? extends SuperTestosterone> clazz) {
        StartServer s = Utils.getServerStarts(clazz);
        if (Utils.isTestosterone(clazz) && s == StartServer.PER_CLASS) {
            start(Utils.getTestosterone(clazz));
        }
    }

    /**
     * Stop configuration after test class.
     *
     * @param clazz
     * @throws java.lang.Exception
     */
    public static void afterClass(Class<? extends SuperTestosterone> clazz) {
        StartServer s = Utils.getServerStarts(clazz);
        if (Utils.isTestosterone(clazz) && s == StartServer.PER_CLASS) {
            stop(Utils.getTestosterone(clazz));
        }
    }

    /**
     * StartServer configuration before test method.
     *
     * @param clazz
     * @throws java.lang.Exception
     */
    public static void before(Class<? extends SuperTestosterone> clazz) {
        before(Utils.getTestosterone(clazz));
    }

    /**
     * StartServer configuration before test method.
     *
     * @param orig
     * @throws java.lang.Exception
     */
    public static void before(@This SuperTestosterone orig) {
        if (Utils.isTestosterone(orig) && orig.getTestConfig().getStartServer() == StartServer.PER_TEST_METHOD) {
            start(orig);
        }
    }

    /**
     * Stop configuration after test method.
     *
     * @param clazz
     * @throws java.lang.Exception
     */
    public static void after(Class<? extends SuperTestosterone> clazz) {
        after(Utils.getTestosterone(clazz));
    }

    /**
     * Stop configuration after test method.
     *
     * @param orig
     * @throws java.lang.Exception
     */
    public static void after(@This SuperTestosterone orig) {
        if (Utils.isTestosterone(orig) && orig.getTestConfig().getStartServer() == StartServer.PER_TEST_METHOD) {
            stop(orig);
        }
    }

    /**
     * TestInterceptors
     */
    public static class Intercept {

        private Intercept() {
        }

        public static class Constructor {

            private Constructor() {
            }

            /**
             * Constructor interceptor
             *
             * @param orig
             * @throws java.lang.IllegalAccessException
             * @throws java.lang.reflect.InvocationTargetException
             * @throws java.lang.NoSuchFieldException
             */
            @RuntimeType
            public static void constructor(@This SuperTestosterone orig) {
                SuperTestosterone main = (SuperTestosterone) orig.getTestConfig().getTest();
                if (!orig.equals(main)) {
                    Utils.copyFields(main, orig);
                }
            }
        }

        /**
         * @PostConstruct interceptor
         */
        public static class PostConstruct {

            private PostConstruct() {
            }

            @RuntimeType
            public static void postConstruct(@This SuperTestosterone orig) {
                LOGGER.debug("Invoking @PostConstruct");
                try {
                    orig.getTestConfig().getSetup().afterServerStart(orig);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        /**
         * @PostConstruct interceptor
         */
        public static class GenericUrl {

            private GenericUrl() {
            }

            @RuntimeType
            public static Void generic(@AllArguments Object[] args, @This SuperTestosterone orig) {
                TestInExecution et = orig.getTestConfig().getSetup().getTestInExecution();
                et.setIsRequest(false);
                try {
                    PathAndTest.test(null, args, et.getMainThreadTestMethod(), et.getMainThreadTest());
                } catch (Throwable ex) {
                    throw new RuntimeException(ex);
                }
                return null;
            }
        }

        public static class Before {

            private Before() {
            }

            /**
             *
             * @param zuper
             * @param method
             * @param orig
             */
            public static void before(@SuperCall Callable<?> zuper, @Origin Method method, @This SuperTestosterone orig) {
                // do nothing
                // it's called later manually in http request scope
            }
        }

        public static class After {

            private After() {
            }

            /**
             *
             * @param zuper
             * @param method
             * @param orig
             */
            public static void after(@SuperCall Callable<?> zuper, @Origin Method method, @This SuperTestosterone orig) {
                // do nothing
                // it's called later manually in http request scope
            }
        }

        /**
         * @AfterClass annotation interceptor
         */
        public static class AfterClass {

            private AfterClass() {
            }

            @RuntimeType
            public static void afterClass(@Origin Method method) {
                TestInterceptors.afterClass((Class<? extends SuperTestosterone>) method.getDeclaringClass());
            }
        }

        /**
         * @BeforeClass annotation interceptor
         */
        public static class BeforeClass {

            private BeforeClass() {
            }

            @RuntimeType
            public static void beforeClass(@Origin Method method) {
                TestInterceptors.beforeClass((Class<? extends SuperTestosterone>) method.getDeclaringClass());
            }
        }

        /**
         * @Path and @Test annotation interceptor
         */
        public static class PathAndTest {

            private PathAndTest() {
            }

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
            public static Object test(@SuperCall Callable<?> zuper, @AllArguments Object[] args, @Origin Method method, @This SuperTestosterone orig) throws NoSuchMethodException, Throwable {

                String invoking = orig.getClass().getName() + ":" + method.getName();

                TestConfig config = orig.getTestConfig();
                Setup setup = config.getSetup();
                ServiceLocator locator = setup.getServiceLocator();
                SuperTestosterone mainThreadTest = (SuperTestosterone) config.getTest();

                Utils.copyFields(mainThreadTest, orig);
                // Flag if this is the main thread in which JUnit test runs
                boolean isMain = Thread.currentThread().getName().equals(config.getMainThreadName());

                // Jersey resource before that will be invoked
                Method resourceMethod = mainThreadTest.getClass().getMethod(method.getName(), method.getParameterTypes());

                TestInExecution et = setup.getTestInExecution();
                // If we are in main JUnit thread, invoke http request to test
                if (isMain) {
                    LOGGER.info("Starting {}", invoking);

                    try {
                        try {
                            Set<Method> methods = mainThreadTest.getTestConfig().getTestMethods();
                            Set<Method> executed = mainThreadTest.getTestConfig().getExecutedTests();
                            methods.remove(resourceMethod);
                            
                            et = new TestInExecutionImpl(orig, mainThreadTest, resourceMethod, Utils.getMethodStartingWithName(mainThreadTest.getClass(), resourceMethod), args);
                            setup.setTestInExecution(et);
                            // Invoking http request to resource before on resource object
                            et.executeTest();
                            executed.add(method);
                        } catch (Exception ex) {
                            LOGGER.error("Failed to execute test " + invoking, ex);
                            config.getExceptions().add(ex.getCause());
                        }
                        // if any, throw errors produced by test before
                        config.throwExceptions();

                    } finally {
                        // clear junit errors
                        config.getExceptions().clear();
                        setup.setRequestsAlreadInvoked(false);
                        LOGGER.info("Ending test {}", invoking);
                    }

                } else {
                    // if thread is not main JUnit thread, then it is http servers thread
                    if (!Utils.hasRequestAnnotation(method) || setup.isRequestsAlreadInvoked()) {
                        et.beforeTest();
                    } else {
                        et.setIsRequest(true);
                    }

                    Utils.inject(locator, orig);
                    try {
                        LOGGER.info("Invoking {}", invoking);
                        for (Method m : Utils.getAnnotatedMethods(orig.getClass(), TestAnnotations.BEFORE)) {
                            Utils.invokeOriginalMethod(m, orig, new Object[]{});
                        }

                        if (Utils.hasRequestAnnotation(method) && !setup.isRequestsAlreadInvoked()) {
                            setup.setRequestsAlreadInvoked(true);
                            et.executeRequests();
                        } else {
                            Request req = method.getAnnotation(Request.class);
                            Path path = method.getAnnotation(Path.class);
                            if (req != null && path != null && path.value().equals(req.url())) {

                            } else {
                                return Utils.invokeOriginalMethod(resourceMethod, orig, args);
                            }
                        }
                    } catch (Throwable ex) {
                        if (ex instanceof InvocationTargetException) {
                            ex = ex.getCause();
                        }

                        // storing error to messages so they can be thrown in main JUnit thread
                        config.getExceptions().add(ex);

                    } finally {
                        et.afterTest();
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
