package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;

/**
 * Container holding different "metadata" belonging to single Testosterone
 * instance.
 *
 * @author stasha
 */
public class Setup {

	private Setup parent;
	private final boolean suite;
	private final Testosterone testosterone;
	private final ServerConfig configuration;
	private TestExecutorImpl executor;

	/**
	 * Creates TestosteroneSetup.
	 *
	 * @param testosterone
	 * @param configuration
	 * @param isSuite
	 */
	public Setup(Testosterone testosterone, ServerConfig configuration, boolean isSuite) {
		if (testosterone == null) {
			throw new IllegalArgumentException("Testosterone instance can't be null");
		}
		if (configuration == null) {
			throw new IllegalArgumentException("Configuration instance can't be null");
		}
		this.testosterone = testosterone;
		this.configuration = configuration;
		this.suite = isSuite;
	}

	/**
	 * Returns testosterone test instance.
	 *
	 * @return
	 */
	public Testosterone getTestosterone() {
		return testosterone;
	}

	/**
	 * Returns configuration used by testosterone test class.
	 *
	 * @return
	 */
	public ServerConfig getConfiguration() {
		return configuration;
	}

	/**
	 * Returns parent Setup. For now this is not used anywhere.
	 *
	 * @return
	 */
	public Setup getParent() {
		return parent;
	}

	/**
	 * Sets parent.
	 *
	 * @param parent
	 */
	public void setParent(Setup parent) {
		this.parent = parent;
	}

	/**
	 * Returns true/false if this setup is suite.
	 *
	 * @return
	 */
	public boolean isSuite() {
		return suite;
	}

}
