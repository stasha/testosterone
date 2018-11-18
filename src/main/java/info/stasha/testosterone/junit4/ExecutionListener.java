package info.stasha.testosterone.junit4;

import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.TestInterceptors;
import info.stasha.testosterone.jersey.Testosterone;
import org.junit.runner.Description;
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
        Class<? extends Testosterone> cls = TestInstrumentation.testClass(
                (Class<? extends Testosterone>) description.getChildren().get(0).getTestClass(),
                new BeforeClassAnnotation(),
                new AfterClassAnnotation()
        );
        
        clazz = cls;
        TestInterceptors.beforeClass(clazz);
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
