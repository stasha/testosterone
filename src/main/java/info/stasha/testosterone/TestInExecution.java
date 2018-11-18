package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import java.lang.reflect.Method;

/**
 * TestInExecution interface.
 *
 * @author stasha
 */
public interface TestInExecution extends TestExecutor {

    /**
     * Executes after test listeners.
     *
     * @throws Exception
     */
    void afterTest() throws Exception;

    /**
     * Executes before test listeners
     *
     * @throws Exception
     */
    void beforeTest() throws Exception;

    /**
     * Executes Test method that is annotated with @Requests or @Request
     * annotations.
     *
     * @throws Throwable
     */
    void executeRequests() throws Throwable;

    /**
     * Executes Test method.
     *
     * @throws Throwable
     */
    void executeTest() throws Throwable;

    /**
     * Passed arguments to test method.
     *
     * @return
     */
    Object[] getArguments();

    /**
     * Returns actual test method that should run.
     *
     * @return
     */
    Method getMainThreadTestMethod();

    /**
     * Returns Testosterone instance that is in main thread.
     *
     * @return
     */
    Testosterone getMainThreadTest();

    /**
     * Original test method that's not intercepted.
     *
     * @return
     */
    Method getOriginMainThreadTestMethod();

    /**
     * Return Testosterone instance that is in server thread.
     *
     * @return
     */
    Testosterone getServerThreadTest();

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
    boolean isTest();

    /**
     * Sets if executed method is test method itself or not.
     *
     * @param isTest
     */
    void setIsTest(boolean isTest);

    /**
     * Returns true/false if currently invoking target is @Request.
     *
     * @return
     */
    boolean isRequest();

    /**
     * Sets if currently invoking target is @Request
     *
     * @param isRequest
     */
    void setIsRequest(boolean isRequest);

}
