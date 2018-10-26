package info.stasha.testosterone.junit;

import info.stasha.testosterone.Testosterone;
import java.util.ArrayList;
import java.util.List;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Class responsible for running @After methods and other after stuff.
 *
 * @author stasha
 */
public class RunAftersRequest extends Statement {

	private final FrameworkMethod method;
	private final Statement next;
	private final Testosterone target;
	private final List<FrameworkMethod> afters;

	public RunAftersRequest(FrameworkMethod method, Statement next, List<FrameworkMethod> afters, Object target) {
		this.method = method;
		this.next = next;
		this.afters = afters;
		this.target = (Testosterone) target;
	}

	@Override
	public void evaluate() throws Throwable {
		List<Throwable> errors = new ArrayList<>();
		try {
			next.evaluate();

			errors.addAll(target.getMessages());

			if (target.getExpectedExceptions().size() > 0) {
				throw target.getExpectedExceptions().get(0);
			}

		} catch (Throwable e) {
			errors.add(e);
		} finally {
			for (FrameworkMethod each : afters) {
				try {
					each.invokeExplosively(target);
				} catch (Throwable e) {
					errors.add(e);
				}
			}

			// cleaning up test
			target.getMessages().clear();
			target.getExpectedExceptions().clear();
		}
		if (errors.isEmpty()) {
			return;
		}
		if (errors.size() == 1) {
			throw errors.get(0);
		}

		throw new MultipleFailureException(errors);
	}
}
