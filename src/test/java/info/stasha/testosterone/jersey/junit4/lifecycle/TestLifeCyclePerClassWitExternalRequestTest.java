package info.stasha.testosterone.jersey.junit4.lifecycle;

import info.stasha.testosterone.annotation.Request;
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
public class TestLifeCyclePerClassWitExternalRequestTest implements Testosterone {

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
    @Request
    public void test() {
    }

    @Test
    @Request
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
         * Why there are duplicate 44 55 44 55?
         *
         * So basically it goes:<br>
         * - @Before @Request (4) -> @Before @Test (4)<br>
         * - @After @Request (5) -> @After @Test (5)<br>
         *
         * and this goes 2x because there are 2 @Test methods.
         *
         * This behavior ensures that we have fully workable testing environment
         * even in @Request stage.
         */
        assertEquals("Number sequence should equal", "12344554455678", SB.toString());
    }

}
