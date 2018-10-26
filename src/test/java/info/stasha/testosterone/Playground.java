package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jerseyon.JerseyConfiguration;
import info.stasha.testosterone.jerseyon.Testosterone;
import info.stasha.testosterone.jerseyon.TestosteroneRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(JerseyConfiguration.class)
//@Configuration(JettyConfiguration.class)
public class Playground implements Testosterone {

	@Before
	public void setUp() {
		System.out.println("setup");
	}

	@After
	public void tearDown() {
		System.out.println("after");
	}

	@Test
	public void test() {
		System.out.println("test");
	}
}
