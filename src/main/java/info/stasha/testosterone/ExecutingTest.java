package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Test that's currently executing.
 *
 * @author stasha
 */
public class ExecutingTest implements TestExecutor {

    private final Testosterone mainThreadTestosterone;
    private final Testosterone serverThreadTestosterone;
    private final Method testMethod;
    private final Method origMethod;
    private final Object[] arguments;

    /**
     * Creates new ExecutingTest instance.
     *
     * @param testMethod
     * @param serverThreadTestosterone
     * @param mainThreadTestosterone
     * @param origMethod
     * @param arguments
     */
    public ExecutingTest(Testosterone mainThreadTestosterone, Testosterone serverThreadTestosterone, Method testMethod, Method origMethod, Object[] arguments) {
        this.mainThreadTestosterone = mainThreadTestosterone;
        this.serverThreadTestosterone = serverThreadTestosterone;
        this.testMethod = testMethod;
        this.origMethod = origMethod;
        this.arguments = arguments;
    }

    /**
     * Returns Testosterone instance that is in main thread.
     *
     * @return
     */
    public Testosterone getMainThreadTestosterone() {
        return mainThreadTestosterone;
    }

    /**
     * Return Testosterone instance that is in server thread.
     *
     * @return
     */
    public Testosterone getServerThreadTestosterone() {
        return serverThreadTestosterone;
    }

    /**
     * Returns actual test method that should run.
     *
     * @return
     */
    public Method getTestMethod() {
        return testMethod;
    }

    /**
     * Original test method that's not intercepted.
     *
     * @return
     */
    public Method getOrigMethod() {
        return origMethod;
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
        mainThreadTestosterone.getConfigFactory().getTestExecutor(testMethod, mainThreadTestosterone).executeTest();
    }

    /**
     * Executes Test method that is annotated with @Requests or @Request
     * annotations.
     *
     * @throws Throwable
     */
    @Override
    public void executeRequests() throws Throwable {
        mainThreadTestosterone.getConfigFactory().getTestExecutor(testMethod, mainThreadTestosterone).executeRequests();
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String toString() {
        return "ExecutingTest{" + "test=" + testMethod + ", testosterone=" + mainThreadTestosterone + ", origMethod=" + origMethod + ", arguments=" + Arrays.toString(arguments) + '}';
    }

}
