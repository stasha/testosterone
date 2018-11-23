package info.stasha.testosterone.junit5;

import info.stasha.testosterone.jersey.junit5.Testosterone;
import info.stasha.testosterone.SuperTestosterone;
import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.TestInterceptors;
import info.stasha.testosterone.Utils;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit5 test execution listener.<br>
 * <p>
 * It is registered in:<br>
 * <code>
 * /META-INF/services/org.junit.platform.launcher.TestExecutionListener
 * </code>
 * </p>
 *
 * @author stasha
 */
public class ExecutionListener implements TestExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionListener.class);

    /**
     * If available, returns instrumented test class.
     *
     * @param ti
     * @return
     */
    private Class<? extends SuperTestosterone> getClass(TestIdentifier ti) {
        TestSource ts = ti.getSource().orElse(null);
        if (ts != null) {
            Class<?> cls = null;
            if (ts instanceof ClassSource) {
                cls = ((ClassSource) ts).getJavaClass();
            } else if (ts instanceof MethodSource) {
                String className = ((MethodSource) ts).getClassName();
                cls = Utils.getClass(className);
            }
            if (Testosterone.class.isAssignableFrom(cls)) {
                return (Class<? extends SuperTestosterone>) TestInstrumentation.testClass(
                        (Class<? extends SuperTestosterone>) cls, new BeforeAllAnnotation(), new AfterAllAnnotation());
            }
        }

        return null;
    }

    /**
     * {@inheritDoc }
     *
     * @param testIdentifier
     */
    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        Class<? extends SuperTestosterone> cls = getClass(testIdentifier);
        if (cls != null && Utils.isTestosterone(cls)) {
            if (testIdentifier.isContainer()) {
                TestInterceptors.beforeClass(cls);
            } else if (testIdentifier.isTest()) {
                TestInterceptors.before(cls);
            }
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param testIdentifier
     * @param testExecutionResult
     */
    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {

        Class<? extends SuperTestosterone> cls = getClass(testIdentifier);
        if (cls != null && Utils.isTestosterone(cls)) {
            if (testIdentifier.isContainer()) {
                TestInterceptors.afterClass(cls);
            } else if (testIdentifier.isTest()) {
                TestInterceptors.after(cls);
            }
        }
    }

}
