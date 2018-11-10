package info.stasha.testosterone;

/**
 * When to start/stop server.
 */
public enum StartServer {
	/**
	 * Server is started/stopped based on parent.<br>
	 * This option will run server only once per suite, in case tests are run
	 * from suite. If there is no parent configuration, server will be started
	 * like PER_CLASS.<br>
	 * This can be overwritten by configuring individual classes in suite. Note
	 * that you will also have to change server port in case you are changing
	 * this configuration on individual classes inside suite.
	 */
	BY_PARENT,
	/**
	 * Server is started/stopped per test class.
	 */
	PER_CLASS,
	/**
	 * Server is started/stopped per test method.
	 */
	PER_TEST,
	/**
	 * Don't start the server.
	 */
	DONT_START
}
