package info.stasha.testosterone.testng;

import info.stasha.testosterone.SuperTestosterone;
import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.TestInterceptors;
import info.stasha.testosterone.Utils;
import org.slf4j.LoggerFactory;
import org.testng.IClassListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Test lifecycle listener.<br>
 * It is registered in /META-INF/services/org.testng.ITestNGListener
 *
 * @author stasha
 */
public class ExecutionListener implements ITestListener, IClassListener {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ExecutionListener.class);

    private Class<? extends SuperTestosterone> getClass(Class<?> obj) {
        return (Class<? extends SuperTestosterone>) TestInstrumentation.getInstrumentedClass((Class<? extends SuperTestosterone>) obj);
    }

    /**
     * {@inheritDoc }
     *
     * @param itc
     */
    @Override
    public void onStart(ITestContext itc) {
        //
    }

    /**
     * {@inheritDoc }
     *
     * @param itc
     */
    @Override
    public void onFinish(ITestContext itc) {
        //
    }

    /**
     * {@inheritDoc }
     *
     * @param itr
     */
    @Override
    public void onTestStart(ITestResult itr) {
        if (!Utils.isTestosterone(itr.getInstance())) {
            return;
        }

        TestInterceptors.before((SuperTestosterone) itr.getInstance());
    }

    /**
     * {@inheritDoc }
     *
     * @param itr
     */
    @Override
    public void onTestSuccess(ITestResult itr) {
        if (!Utils.isTestosterone(itr.getInstance())) {
            return;
        }

        TestInterceptors.after((SuperTestosterone) itr.getInstance());
    }

    /**
     * {@inheritDoc }
     *
     * @param itr
     */
    @Override
    public void onTestFailure(ITestResult itr) {
        if (!Utils.isTestosterone(itr.getInstance())) {
            return;
        }

        TestInterceptors.after((SuperTestosterone) itr.getInstance());
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
        if (!Utils.isTestosterone(itr.getInstance())) {
            return;
        }

        TestInterceptors.after((SuperTestosterone) itr.getInstance());
    }

    @Override
    public void onBeforeClass(ITestClass itc) {
        if (!Utils.isTestosterone(itc.getRealClass())) {
            return;
        }

        TestInterceptors.beforeClass(getClass(itc.getRealClass()));
    }

    @Override
    public void onAfterClass(ITestClass itc) {
        if (!Utils.isTestosterone(itc.getRealClass())) {
            return;
        }

        TestInterceptors.afterClass(getClass(itc.getRealClass()));
    }

}
