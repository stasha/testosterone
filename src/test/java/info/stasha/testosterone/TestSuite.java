package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.jersey.injectables.InjectablesTest;
import info.stasha.testosterone.junit4.TestosteroneSuiteRunner;
import info.stasha.testosterone.servlet.servletfilter.ServletFilterTest;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@Ignore("Fails on building servers, but passes locally")
@RunWith(TestosteroneSuiteRunner.class)
@org.junit.runners.Suite.SuiteClasses({
	ServletFilterTest.class,
	PlaygroundTest.class,
	InjectablesTest.class
})
public class TestSuite implements Testosterone {
	
}
