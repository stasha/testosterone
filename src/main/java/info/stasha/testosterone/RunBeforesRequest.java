package info.stasha.testosterone;

import java.lang.reflect.Method;
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
	private final JerseyRequestTest target;

	public RunBeforesRequest(FrameworkMethod method, Statement next, List<FrameworkMethod> befores, Object target) {
		this.method = method;
		this.next = next;
		this.befores = befores;
		this.target = (JerseyRequestTest) target;
	}

	@Override
	public void evaluate() throws Throwable {
		Method m = target.getClass().getMethod("setRequestTest", Boolean.class);
		m.invoke(target, true);

		for (FrameworkMethod before : befores) {
			before.invokeExplosively(target);
		}

		next.evaluate();
	}
}
