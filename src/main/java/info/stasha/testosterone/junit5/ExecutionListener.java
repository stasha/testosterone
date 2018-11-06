package info.stasha.testosterone.junit5;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.Interceptors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClassSource;
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
	private Class<?> getClass(TestIdentifier ti) {
		TestSource ts = ti.getSource().orElse(null);
		if (ts != null && ts instanceof ClassSource) {
			Class<?> cls = ((ClassSource) ts).getJavaClass();
			if (Testosterone.class.isAssignableFrom(cls)) {
				return Instrument.testClass(
						cls,
						new BeforeEachAnnotation(),
						new AfterEachAnnotation(),
						new BeforeAllAnnotation(),
						new AfterAllAnnotation()
				);
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param testIdentifier
	 * @param testExecutionResult
	 */
	@Override
	public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
		TestExecutionListener.super.executionFinished(testIdentifier, testExecutionResult);

		Class<?> cls = getClass(testIdentifier);
		if (testIdentifier.isContainer() && cls != null) {
			try {
				Interceptors.Intercept.AfterClass.afterClass(cls);
			} catch (Exception ex) {
				Logger.getLogger(ExecutionListener.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * {@inheritDoc }
	 *
	 * @param testIdentifier
	 */
	@Override
	public void executionStarted(TestIdentifier testIdentifier) {
		Class<?> cls = getClass(testIdentifier);
		if (testIdentifier.isContainer() && cls != null) {
			try {
				Interceptors.Intercept.BeforeClass.beforeClass(cls);
			} catch (Exception ex) {
				Logger.getLogger(ExecutionListener.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		TestExecutionListener.super.executionStarted(testIdentifier);

	}

}
