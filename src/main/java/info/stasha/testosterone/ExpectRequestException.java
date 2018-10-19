package info.stasha.testosterone;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 *
 * @author stasha
 */
public class ExpectRequestException extends Statement {

	private final FrameworkMethod method;
	private final Statement next;
	private final Class<? extends Throwable> expected;
	private final JerseyRequestTest target;

	public ExpectRequestException(FrameworkMethod method, Statement next, Object target, Class<? extends Throwable> expected) {
		this.method = method;
		this.next = next;
		this.target = (JerseyRequestTest) target;
		this.expected = expected;
	}

	@Override
	public void evaluate() throws Exception {
		boolean complete = false;
		try {
			next.evaluate();
			if (target.getThrownException() != null) {
				throw target.getThrownException();
			}
			complete = true;
		} catch (AssumptionViolatedException e) {
			throw e;
		} catch (Throwable e) {
			if (!expected.isAssignableFrom(e.getClass())) {
				String message = "Unexpected exception, expected<"
						+ expected.getName() + "> but was<"
						+ e.getClass().getName() + ">";
				throw new Exception(message, e);
			}
		} finally {
			target.setThrownException(null);
		}
		if (complete) {
			throw new AssertionError("Expected exception: "
					+ expected.getName());
		}
	}
}
