package info.stasha.testosterone.testng;

import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.TestIterceptors;
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
	
	/**
	 * {@inheritDoc }
	 * 
	 * @param itc 
	 */
	@Override
	public void onStart(ITestContext itc) {
		Class<? extends Testosterone> clazz = TestInstrumentation.getInstrumentedClass(itc.getAllTestMethods()[0].getRealClass());
		TestIterceptors.beforeClass(clazz);
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @param itc 
	 */
	@Override
	public void onFinish(ITestContext itc) {
		Class<? extends Testosterone> clazz = TestInstrumentation.getInstrumentedClass(itc.getAllTestMethods()[0].getRealClass());
		TestIterceptors.afterClass(clazz);
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @param itr 
	 */
	@Override
	public void onTestStart(ITestResult itr) {
		TestIterceptors.before((Testosterone) itr.getInstance());
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @param itr 
	 */
	@Override
	public void onTestSuccess(ITestResult itr) {
		TestIterceptors.after((Testosterone) itr.getInstance());
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @param itr 
	 */
	@Override
	public void onTestFailure(ITestResult itr) {
		TestIterceptors.after((Testosterone) itr.getInstance());
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @param itr 
	 */
	@Override
	public void onTestSkipped(ITestResult itr) {
//		System.out.println(itr);
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @param itr 
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult itr) {
		TestIterceptors.after((Testosterone) itr.getInstance());
//		System.out.println(itr);
	}

	

}
