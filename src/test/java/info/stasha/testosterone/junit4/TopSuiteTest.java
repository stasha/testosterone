package info.stasha.testosterone.junit4;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.Testosterone;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneSuiteRunner.class)
@org.junit.runners.Suite.SuiteClasses({
	RunTestsAndSuitesSuite.class
})
@Configuration(serverStarts = Configuration.ServerStarts.DONT_START)
public class TopSuiteTest implements Testosterone {

}
