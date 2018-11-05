package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;

/**
 *
 * @author stasha
 */
public class TestosteroneSetup {

	private TestosteroneSetup parent;
	private boolean suite;
	private final Testosterone testosterone;
	private final TestosteroneConfig configuration;

	public TestosteroneSetup(Testosterone testosterone, TestosteroneConfig configuration, boolean isSuite) {
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

	public Testosterone getTestosterone() {
		return testosterone;
	}

	public TestosteroneConfig getConfiguration() {
		return configuration;
	}

	public TestosteroneSetup getParent() {
		return parent;
	}

	public void setParent(TestosteroneSetup parent) {
		this.parent = parent;
	}

	public boolean isSuite() {
		return suite;
	}

	public void setSuite(boolean suite) {
		this.suite = suite;
	}

}
