package info.stasha.testosterone.jersey.junit4;

import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Http methods test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class PlaygroundTest implements Testosterone {


    @Test
    public void test() {
        System.out.println("test");
    }

}
