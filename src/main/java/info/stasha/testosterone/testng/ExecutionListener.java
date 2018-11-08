package info.stasha.testosterone.testng;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.Interceptors;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Test lifecycle listener.<br>
 * It is registered in /META-INF/services/org.testng.ITestNGListener
 *
 * @author stasha
 */
public class ExecutionListener implements ITestListener {
	
	@Override
	public void onStart(ITestContext itc) {
		Class<?> clazz = Instrument.getInstrumentedClass(itc.getAllTestMethods()[0].getRealClass());
		Interceptors.Intercept.BeforeClass.beforeClass(clazz);
	}

	@Override
	public void onFinish(ITestContext itc) {
		Class<?> clazz = Instrument.getInstrumentedClass(itc.getAllTestMethods()[0].getRealClass());
		Interceptors.Intercept.AfterClass.afterClass(clazz);
	}

	@Override
	public void onTestStart(ITestResult itr) {
		Interceptors.Intercept.Before.before((Testosterone) itr.getInstance());
	}

	@Override
	public void onTestSuccess(ITestResult itr) {
		Interceptors.Intercept.After.after((Testosterone) itr.getInstance());
	}

	@Override
	public void onTestFailure(ITestResult itr) {
		Interceptors.Intercept.After.after((Testosterone) itr.getInstance());
	}

	@Override
	public void onTestSkipped(ITestResult itr) {
//		System.out.println(itr);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult itr) {
		Interceptors.Intercept.After.after((Testosterone) itr.getInstance());
//		System.out.println(itr);
	}

	

}
