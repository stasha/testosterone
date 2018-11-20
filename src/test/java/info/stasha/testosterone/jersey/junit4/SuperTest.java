package info.stasha.testosterone.jersey.junit4;

import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class SuperTest implements Testosterone {

	@BeforeClass
	public static void beforeClass() {
		System.out.println("super internal before class");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("super internal after class");
	}

	@Before
	public void superBefore() {
		System.out.println("super before");
	}

	@After
	public void superAfter() {
		System.out.println("super after");
	}

	@Test
	public void superTest() {
		System.out.println("super test");
	}

}
