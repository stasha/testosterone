package info.stasha.testosterone.jersey.junit4.lifecycle;

import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.BeforeClass;

/**
 * Testing test lifecycle.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class TestLifeCyclePerClassTest implements Testosterone {

    protected static final StringBuilder SB = new StringBuilder();

    @Override
    public void beforeServerStart() throws Exception {
        SB.append(1);
    }

    @BeforeClass
    public static void beforeClass() {
        SB.append(2);
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

        assertEquals("Number sequence should equal", "1234545678", SB.toString());
    }

}
