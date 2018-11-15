package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.Method;

/**
 * Test that's currently executing.
 *
 * @author stasha
 */
public class TestInExecution implements TestExecutor {

    private final Testosterone maniThreadTest;
    private final Testosterone serverThreadTest;
    private final Method mainThreadTestMethod;
    private final Method originMainThreadTestMethod;
    private final Object[] arguments;

    /**
     * Creates new TestInExecution instance.
     *
     * @param mainThreadTest
     * @param serverTrhreadTest
     * @param mainThreadTestMethod
     * @param originMainThreadTestMethod
     * @param arguments
     */
    public TestInExecution(Testosterone mainThreadTest,
            Testosterone serverTrhreadTest,
            Method mainThreadTestMethod,
            Method originMainThreadTestMethod,
            Object[] arguments) {
        this.maniThreadTest = mainThreadTest;
        this.serverThreadTest = serverTrhreadTest;
        this.mainThreadTestMethod = mainThreadTestMethod;
        this.originMainThreadTestMethod = originMainThreadTestMethod;
        this.arguments = arguments;
    }

    /**
     * Returns Testosterone instance that is in main thread.
     *
     * @return
     */
    public Testosterone getManiThreadTest() {
        return maniThreadTest;
    }

    /**
     * Return Testosterone instance that is in server thread.
     *
     * @return
     */
    public Testosterone getServerThreadTest() {
        return serverThreadTest;
    }

    /**
     * Returns actual test method that should run.
     *
     * @return
     */
    public Method getMainThreadTestMethod() {
        return mainThreadTestMethod;
    }

    /**
     * Original test method that's not intercepted.
     *
     * @return
     */
    public Method getOriginMainThreadTestMethod() {
        return originMainThreadTestMethod;
    }

    /**
     * Passed arguments to test method.
     *
     * @return
     */
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * Executes Test method.
     *
     * @throws Throwable
     */
    @Override
    public void executeTest() throws Throwable {
        maniThreadTest.getConfigFactory().getTestExecutor(mainThreadTestMethod, maniThreadTest).executeTest();
    }

    /**
     * Executes Test method that is annotated with @Requests or @Request
     * annotations.
     *
     * @throws Throwable
     */
    @Override
    public void executeRequests() throws Throwable {
        maniThreadTest.getConfigFactory().getTestExecutor(mainThreadTestMethod, maniThreadTest).executeRequests();
    }

}
