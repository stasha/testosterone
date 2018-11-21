package info.stasha.testosterone.jersey.junit4;

import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class PlaygroundTest implements Testosterone {

    @Test
    public void test(Response resp) {
    }

}
