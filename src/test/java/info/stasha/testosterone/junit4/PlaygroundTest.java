package info.stasha.testosterone.junit4;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.Testosterone;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(serverStarts = StartServer.PER_CLASS)
public class PlaygroundTest implements Testosterone {

	@Test
	public void test() {

	}

}
