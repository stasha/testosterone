package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.jersey.service.ServiceTest;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneSuiteRunner.class)
@org.junit.runners.Suite.SuiteClasses({
	PlaygroundTest.class,
	ServiceTest.class
})
public class TestSuite implements Testosterone {

}
