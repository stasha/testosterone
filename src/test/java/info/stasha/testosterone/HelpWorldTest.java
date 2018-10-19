package info.stasha.testosterone;

import javax.ws.rs.core.Context;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JerseyRequestTestRunner.class)
public class HelpWorldTest extends MyAppSuperTest {

	@Context
	private MyService myService;

	@RequestTest
	public void test() {
		System.out.println(myService.getText());
		assertTrue("true is true", true);
	}

	@RequestTest
	public void test2() {
		System.out.println(myService.getText());
	}

	@Test
	public void test3() {
		assertTrue(true);
	}

}
