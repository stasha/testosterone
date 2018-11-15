package info.stasha.testosterone.junit4;

import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.TestInterceptors;
import info.stasha.testosterone.jersey.Testosterone;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

/**
 * Junit4 Test listener.
 *
 * @author stasha
 */
public class ExecutionListener extends RunListener {

	// HACK BECAUSE TESTRUNFINISHED DOES NOT WORK
	private static Class<? extends Testosterone> clazz;

	/**
	 * {@inheritDoc }
	 *
	 * @param description
	 * @throws Exception
	 */
	@Override
	public void testRunStarted(Description description) throws Exception {
		Class<? extends Testosterone> cls = TestInstrumentation.testClass((Class<? extends Testosterone>) description.getChildren().get(0).getTestClass(), new AfterClassAnnotation());
		if (clazz != null && clazz != cls) {
			// HACK BECAUSE TESTRUNFINISHED DOES NOT WORK
			TestInterceptors.afterClass(clazz);
		}
		clazz = cls;
		TestInterceptors.beforeClass(clazz);
	}

	/**
	 * {@inheritDoc }
	 *
	 * !!! NOTE !!! THIS DOES NOT WORK SO WE MUST HACK IT USING STATIC CLAZZ,
	 * AND/OR WITH AFTERCLASS INTERCEPTOR :(
	 *
	 * @param description
	 * @throws Exception
	 */
	@Override
	public void testRunFinished(Result description) throws Exception {
		TestInterceptors.afterClass(clazz);
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param description
	 * @throws Exception
	 */
	public void testRunFinished(Description description) throws Exception {
		TestInterceptors.afterClass(clazz);
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param description
	 * @throws Exception
	 */
	@Override
	public void testStarted(Description description) throws Exception {
		TestInterceptors.before(clazz);
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param description
	 * @throws Exception
	 */
	@Override
	public void testFinished(Description description) throws Exception {
		TestInterceptors.after(clazz);
	}

}
