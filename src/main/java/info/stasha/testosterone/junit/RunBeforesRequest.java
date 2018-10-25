package info.stasha.testosterone.junit;

import info.stasha.testosterone.Testosterone;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Class responsible for running @Before methods and other before stuff.
 *
 * @author stasha
 */
public class RunBeforesRequest extends Statement {

	private final FrameworkMethod method;
	private final Statement next;
	private final List<FrameworkMethod> befores;
	private final Testosterone target;

	public RunBeforesRequest(FrameworkMethod method, Statement next, List<FrameworkMethod> befores, Object target) {
		this.method = method;
		this.next = next;
		this.befores = befores;
		this.target = (Testosterone) target;
	}

	@Override
	public void evaluate() throws Throwable {

		for (FrameworkMethod before : befores) {
			before.invokeExplosively(target);
		}

		next.evaluate();
	}
}
