package info.stasha.testosterone.junit5;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.Interceptors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

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
				try {
					cls = Class.forName(((MethodSource) ts).getClassName());
				} catch (ClassNotFoundException ex) {
					Logger.getLogger(ExecutionListener.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			if (Testosterone.class.isAssignableFrom(cls)) {
				return (Class<? extends Testosterone>) Instrument.testClass((Class<? extends Testosterone>) cls, new AfterAllAnnotation());
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
		if (testIdentifier.isContainer() && cls != null) {
			Interceptors.Intercept.BeforeClass.beforeClass(cls);
		} else if (testIdentifier.isTest()) {
			Interceptors.Intercept.Before.before(cls);
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
		if (testIdentifier.isContainer() && cls != null) {
			Interceptors.Intercept.AfterClass.afterClass(cls);
		} else if (testIdentifier.isTest()) {
			Interceptors.Intercept.After.after(cls);
		}
	}

}
