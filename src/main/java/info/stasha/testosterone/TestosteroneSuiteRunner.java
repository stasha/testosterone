package info.stasha.testosterone;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 *
 * @author stasha
 */
public class TestosteroneSuiteRunner extends org.junit.runners.Suite {

	public TestosteroneSuiteRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
	}

}
