package info.stasha.testosterone;

/**
 * Test executor.
 *
 * @author stasha
 */
public interface TestExecutor {

    /**
     * Executes method annotated with @Test.
     *
     * @throws Throwable
     */
    void executeTest() throws Throwable;

    /**
     * Executes tests annotated with @Requests or @Request annotation
     *
     * @throws Throwable
     */
    void executeRequests() throws Throwable;

}
