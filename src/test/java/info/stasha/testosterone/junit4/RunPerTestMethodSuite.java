package info.stasha.testosterone.junit4;

import info.stasha.testosterone.PlaygroundTest;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.jersey.injectables.InjectablesTest;
import info.stasha.testosterone.servlet.servletfilter.ServletFilterTest;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneSuiteRunner.class)
@org.junit.runners.Suite.SuiteClasses({
	ServletFilterTest.class,
	PlaygroundTest.class,
	InjectablesTest.class
})
@Configuration(port = 9998, serverStarts = Configuration.ServerStarts.PER_TEST)
public class RunPerTestMethodSuite implements Testosterone {

}
