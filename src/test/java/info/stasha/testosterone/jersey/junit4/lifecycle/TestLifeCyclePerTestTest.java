package info.stasha.testosterone.jersey.junit4.lifecycle;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing test lifecycle when invoked in per_test mode.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(startServer = StartServer.PER_TEST_METHOD)
public class TestLifeCyclePerTestTest implements Testosterone {

    protected static final StringBuilder SB = new StringBuilder();

    @BeforeClass
    public static void beforeClass() {
        SB.append(2);
    }

    /**
     * When running in per test mode, @BeforeClass is executed before
     * beforeServerStart. This is because server is started in
     *
     * @throws java.lang.Exception
     * @Before/@After manner.
     */
    @Override
    public void beforeServerStart() throws Exception {
        SB.append(1);
    }

    @Override
    public void afterServerStart() throws Exception {
        SB.append(3);
    }

    @Before
    public void before() {
        SB.append(4);
    }

    @Test
    public void test() {
    }

    @Test
    public void test2() {
    }

    @After
    public void after() {
        SB.append(5);
    }

    @Override
    public void beforeServerStop() throws Exception {
        SB.append(6);
    }

    @Override
    public void afterServerStop() throws Exception {
        SB.append(7);
    }

    @AfterClass
    public static void afterClass() {
        SB.append(8);
        /**
         * There are 2 tests so numbers are repeating, except first and last
         * that are @BeforeClass adn @AfterClass
         */

        assertEquals("Number sequence should equal", "21345671345678", SB.toString());
    }

}
