package info.stasha.testosterone.junit4;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.jersey.expectedexception.ExpectedExceptionTest;
import info.stasha.testosterone.servlet.servletlistener.ServletListenerTest;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneSuiteRunner.class)
@org.junit.runners.Suite.SuiteClasses({
	ServletListenerTest.class,
	ExpectedExceptionTest.class
})
@Configuration(port = 9998, serverStarts = Configuration.ServerStarts.PER_CLASS)
public class RunPerTestClassSuite implements Testosterone {

}
