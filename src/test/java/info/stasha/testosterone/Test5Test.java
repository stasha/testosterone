package info.stasha.testosterone;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import info.stasha.testosterone.junit5.Testosterone;

public class Test5Test implements Testosterone {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("before");
	}

	@AfterEach
	public void tearDown() throws Exception {
		System.out.println("after");
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
