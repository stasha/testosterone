package info.stasha.testosterone;

/**
 * JUnit test executor.
 *
 * @author stasha
 */
public interface TestExecutor {

	/**
	 * Executes http request/s to JUnit tests.
	 *
	 * @throws Throwable
	 */
	void execute() throws Throwable;

}
