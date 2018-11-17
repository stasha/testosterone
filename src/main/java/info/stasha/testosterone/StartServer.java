package info.stasha.testosterone;

/**
 * When to start/stop server.
 */
public enum StartServer {
	/**
	 * Server is started/stopped per test class.
	 */
	PER_CLASS,
	/**
	 * Server is started/stopped per test method.
	 */
	PER_TEST_METHOD,
	/**
	 * Don't start the server.
	 */
	DONT_START
}
