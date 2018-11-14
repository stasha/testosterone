package info.stasha.testosterone.junit4;

import info.stasha.testosterone.Start;
import info.stasha.testosterone.annotation.Configuration;

/**
 *
 * @author stasha
 */
@Configuration(serverStarts = Start.PER_CLASS, port = 9998)
public class PlaygrountPerClassTest extends PlaygroundTest {

}
