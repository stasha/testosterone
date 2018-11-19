package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TestInterceptors.class);

    /**
     * Starts the configuration.
     *
     * @param t
     */
    private static void start(Testosterone t) throws Exception {
        t.getTestConfig().start();
    }

    /**
     * Stops the configuration.
     *
     * @param t
     */
    private static void stop(Testosterone t) throws Exception {
        t.getTestConfig().stop();
    }

    /**
     * StartServer configuration before test class.
     *
     * @param clazz
     */
    public static void beforeClass(Class<? extends Testosterone> clazz) throws Exception {
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
    public static void afterClass(Class<? extends Testosterone> clazz) throws Exception {
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
    public static void before(Class<? extends Testosterone> clazz) throws Exception {
        before(Utils.getTestosterone(clazz));
    }

    /**
     * StartServer configuration before test method.
     *
     * @param orig
     * @throws java.lang.Exception
     */
    public static void before(@This Testosterone orig) throws Exception {
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
    public static void after(Class<? extends Testosterone> clazz) throws Exception {
        after(Utils.getTestosterone(clazz));
    }

    /**
     * Stop configuration after test method.
     *
     * @param orig
     * @throws java.lang.Exception
     */
    public static void after(@This Testosterone orig) throws Exception {
        if (Utils.isTestosterone(orig) && orig.getTestConfig().getStartServer() == StartServer.PER_TEST_METHOD) {
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
                Testosterone main = orig.getTestConfig().getTest();
                if (!orig.equals(main)) {
                    Utils.copyFields(main, orig);
                }
            }
        }

        /**
         * @PostConstruct interceptor
         */
        public static class PostConstruct {

            @RuntimeType
            public static void postConstruct(@This Testosterone orig) throws Exception {
                LOGGER.debug("Invoking @PostConstruct");
                orig.getTestConfig().getSetup().afterServerStart(orig);
            }
        }

        /**
         * @PostConstruct interceptor
         */
        public static class GenericUrl {

            @RuntimeType
            public static Void generic(@This Testosterone orig) throws Exception, Throwable {
                TestInExecution et = orig.getTestConfig().getSetup().getTestInExecution();
                et.setIsRequest(false);
                PathAndTest.test(null, null, et.getMainThreadTestMethod(), et.getMainThreadTest());
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

                TestConfig config = orig.getTestConfig();
                Setup setup = config.getSetup();
                ServiceLocator locator = setup.getServiceLocator();
                Testosterone mainThreadTest = config.getTest();

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
                            et = new TestInExecutionImpl(orig, mainThreadTest, resourceMethod, Utils.getMethodStartingWithName(mainThreadTest.getClass(), resourceMethod), args);
                            setup.setTestInExecution(et);
                            // Invoking http request to resource before on resource object
                            et.executeTest();
                        } catch (InvocationTargetException ex) {
                            throw ex.getCause();
                        } catch (Exception ex) {
                            LOGGER.error("Failed to execute test " + invoking, ex);
                            config.getExceptions().add(ex);
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

                    locator.inject(orig);
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
                        ex.printStackTrace();
                        System.out.println("-----");
                        ex.getCause().printStackTrace();
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