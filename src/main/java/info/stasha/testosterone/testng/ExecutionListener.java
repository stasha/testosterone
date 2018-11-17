package info.stasha.testosterone.testng;

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

    private Class<? extends Testosterone> getClass(Class<?> obj) {
        return (Class<? extends Testosterone>) TestInstrumentation.getInstrumentedClass((Class<? extends info.stasha.testosterone.jersey.Testosterone>) obj);
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
        try {
            TestInterceptors.before((Testosterone) itr.getInstance());
        } catch (Exception ex) {
            LOGGER.error("Failed to start test", ex);
            throw new RuntimeException(ex);
        }
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
        try {
            TestInterceptors.after((Testosterone) itr.getInstance());
        } catch (Exception ex) {
            LOGGER.error("Failed to finish test", ex);
            throw new RuntimeException(ex);
        }
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
        try {
            TestInterceptors.after((Testosterone) itr.getInstance());
        } catch (Exception ex) {
            LOGGER.error("Failed to finish test", ex);
            throw new RuntimeException(ex);
        }
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
        try {
            TestInterceptors.after((Testosterone) itr.getInstance());
        } catch (Exception ex) {
            LOGGER.error("Failed to finish test", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onBeforeClass(ITestClass itc) {
        if (!Utils.isTestosterone(itc.getRealClass())) {
            return;
        }
        try {
            TestInterceptors.beforeClass(getClass(itc.getRealClass()));
        } catch (Exception ex) {
            LOGGER.error("Failed to start test", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onAfterClass(ITestClass itc) {
        if (!Utils.isTestosterone(itc.getRealClass())) {
            return;
        }
        try {
            TestInterceptors.afterClass(getClass(itc.getRealClass()));
        } catch (Exception ex) {
            LOGGER.error("Failed to finish test", ex);
            throw new RuntimeException(ex);
        }
    }

}
