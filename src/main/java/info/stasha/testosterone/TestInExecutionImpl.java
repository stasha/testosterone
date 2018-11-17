package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test that's currently executing.
 *
 * @author stasha
 */
public class TestInExecutionImpl implements TestInExecution {

    private final Testosterone mainThreadTest;
    private final Testosterone serverThreadTest;
    private final Method mainThreadTestMethod;
    private final Method originMainThreadTestMethod;
    private final Object[] arguments;
    private boolean isTest;
    private boolean isRequest;

    private final Set<TestEventListener> beforeTest = new LinkedHashSet<>();
    private final Set<TestEventListener> afterTest = new LinkedHashSet<>();

    /**
     * Creates new TestInExecution instance.
     *
     * @param mainThreadTest
     * @param serverTrhreadTest
     * @param mainThreadTestMethod
     * @param originMainThreadTestMethod
     * @param arguments
     */
    public TestInExecutionImpl(Testosterone mainThreadTest,
            Testosterone serverTrhreadTest,
            Method mainThreadTestMethod,
            Method originMainThreadTestMethod,
            Object[] arguments) {
        this.mainThreadTest = mainThreadTest;
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
    @Override
    public Testosterone getMainThreadTest() {
        return mainThreadTest;
    }

    /**
     * Return Testosterone instance that is in server thread.
     *
     * @return
     */
    @Override
    public Testosterone getServerThreadTest() {
        return serverThreadTest;
    }

    /**
     * Returns actual test method that should run.
     *
     * @return
     */
    @Override
    public Method getMainThreadTestMethod() {
        return mainThreadTestMethod;
    }

    /**
     * Original test method that's not intercepted.
     *
     * @return
     */
    @Override
    public Method getOriginMainThreadTestMethod() {
        return originMainThreadTestMethod;
    }

    /**
     * Passed arguments to test method.
     *
     * @return
     */
    @Override
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * Returns true if invoking method is really test method. There are two
     * different scenarios when invoking test method, one is when test method
     * has only @Test annotation and the other is when it has @Test and @Request
     * annotations.<br>
     * If there is @Request annotation, then first is invoked @Request
     * annotation which doesn't behave like test and the next is real @Test.
     *
     * @return
     */
    @Override
    public boolean isTest() {
        return isTest;
    }

    /**
     * Sets if executed method is test method itself or not.
     *
     * @param isTest
     */
    @Override
    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isRequest() {
        return isRequest;
    }

    /**
     * {@inheritDoc }
     *
     * @param isRequest
     */
    @Override
    public void setIsRequest(boolean isRequest) {
        this.isRequest = isRequest;
    }

    /**
     * Adds event
     *
     * @param listener
     */
    @Override
    public void addBeforeTestListeenr(TestEventListener listener) {
        beforeTest.add(listener);
    }

    /**
     * {@inheritDoc }
     *
     * @param listener
     */
    @Override
    public void addAfterTestListener(TestEventListener listener) {
        afterTest.add(listener);
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void beforeTest() throws Exception {
        isTest = true;
        isRequest = false;
        while (beforeTest.iterator().hasNext()) {
            TestEventListener listener = beforeTest.iterator().next();
            try {
                listener.trigger();
            } finally {
                beforeTest.remove(listener);
            }
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void afterTest() throws Exception {
        isTest = false;
        isRequest = false;
        while (afterTest.iterator().hasNext()) {
            TestEventListener listener = afterTest.iterator().next();
            try {
                listener.trigger();
            } finally {
                afterTest.remove(listener);
            }
        }
    }

    /**
     * Executes Test method.
     *
     * @throws Throwable
     */
    @Override
    public void executeTest() throws Throwable {
        mainThreadTest.getTestConfig().getTestExecutor(mainThreadTestMethod, mainThreadTest).executeTest();
    }

    /**
     * Executes Test method that is annotated with @Requests or @Request
     * annotations.
     *
     * @throws Throwable
     */
    @Override
    public void executeRequests() throws Throwable {
        mainThreadTest.getTestConfig().getTestExecutor(mainThreadTestMethod, mainThreadTest).executeRequests();
    }

    @Override
    public String toString() {
        return "TestInExecutionImpl{"
                + "test=" + mainThreadTest.getClass().getName() + ":" + mainThreadTestMethod.getName()
                + ", isTest=" + isTest + ", "
                + "isRequest=" + isRequest + '}';
    }

}
