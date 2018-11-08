package info.stasha.testosterone.junit4;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.Interceptors;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class ExecutionListener extends RunListener {

	// HACK BECAUSE TESTRUNFINISHED DOES NOT WORK
	private static Class<?> clazz;

	@Override
	public void testRunStarted(Description description) throws Exception {
		Class<?> cls = Instrument.testClass(description.getChildren().get(0).getTestClass());
		if (clazz != null && clazz != cls) {
			// HACK BECAUSE TESTRUNFINISHED DOES NOT WORK
			Interceptors.Intercept.AfterClass.afterClass(clazz);
		}
		clazz = cls;
		Interceptors.Intercept.BeforeClass.beforeClass(clazz);
	}

	// THIS DOES NOT WORK SO WE MUST HACK IT USING STATIC CLAZZ :( 
	@Override
	public void testRunFinished(Result description) throws Exception {
		Interceptors.Intercept.AfterClass.afterClass(clazz);
	}

	public void testRunFinished(Description description) throws Exception {
		Interceptors.Intercept.AfterClass.afterClass(clazz);
	}

	@Override
	public void testStarted(Description description) throws Exception {
//		Class<?> clazz = description.getTestClass();
		Interceptors.Intercept.Before.before(clazz);
	}

	@Override
	public void testFinished(Description description) throws Exception {
//		Class<?> clazz = description.getTestClass();
		Interceptors.Intercept.After.after(clazz);
	}

}
