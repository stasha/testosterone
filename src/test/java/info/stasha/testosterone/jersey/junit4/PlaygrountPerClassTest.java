package info.stasha.testosterone.jersey.junit4;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;
import org.junit.Ignore;

/**
 *
 * @author stasha
 */
@Ignore
@Configuration(startServer = StartServer.PER_CLASS, httpPort = 9998)
public class PlaygrountPerClassTest extends PlaygroundTest {

}
