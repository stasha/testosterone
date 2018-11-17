package info.stasha.testosterone.junit4;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;

/**
 *
 * @author stasha
 */
@Configuration(startServer = StartServer.PER_CLASS, httpPort = 9998)
public class PlaygrountPerClassTest extends PlaygroundTest {

}
