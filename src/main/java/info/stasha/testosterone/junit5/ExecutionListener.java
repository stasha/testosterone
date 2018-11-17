package info.stasha.testosterone.junit5;

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
    private Class<? extends Testosterone> getClass(TestIdentifier ti) {
        TestSource ts = ti.getSource().orElse(null);
        if (ts != null) {
            Class<?> cls = null;
            if (ts instanceof ClassSource) {
                cls = ((ClassSource) ts).getJavaClass();
            } else if (ts instanceof MethodSource) {
                String className = ((MethodSource) ts).getClassName();
                try {
                    cls = Class.forName(className);
                } catch (ClassNotFoundException ex) {
                    LOGGER.error("Failed to load class: " + className, ex);
                    throw new RuntimeException(ex);
                }
            }
            if (Testosterone.class.isAssignableFrom(cls)) {
                return (Class<? extends Testosterone>) TestInstrumentation.testClass(
                        (Class<? extends Testosterone>) cls, new BeforeAllAnnotation(), new AfterAllAnnotation());
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
        Class<? extends Testosterone> cls = getClass(testIdentifier);
        if (cls != null && Utils.isTestosterone(cls)) {
            if (testIdentifier.isContainer()) {
                try {
                    TestInterceptors.beforeClass(cls);
                } catch (Exception ex) {
                    LOGGER.error("Failed to start test", ex);
                    throw new RuntimeException(ex);
                }
            } else if (testIdentifier.isTest()) {
                try {
                    TestInterceptors.before(cls);
                } catch (Exception ex) {
                    LOGGER.error("Failed to start test", ex);
                    throw new RuntimeException(ex);
                }
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

        Class<? extends Testosterone> cls = getClass(testIdentifier);
        if (cls != null && Utils.isTestosterone(cls)) {
            if (testIdentifier.isContainer()) {
                try {
                    TestInterceptors.afterClass(cls);
                } catch (Exception ex) {
                    LOGGER.error("Failed to finish test", ex);
                    throw new RuntimeException(ex);
                }
            } else if (testIdentifier.isTest()) {
                try {
                    TestInterceptors.after(cls);
                } catch (Exception ex) {
                    LOGGER.error("Failed to finish test", ex);
                    throw new RuntimeException(ex);
                }
            }
        }
    }

}
