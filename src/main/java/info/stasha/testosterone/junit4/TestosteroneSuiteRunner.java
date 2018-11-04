package info.stasha.testosterone.junit4;

import info.stasha.testosterone.Instrument;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 *
 * @author stasha
 */
public class TestosteroneSuiteRunner extends org.junit.runners.Suite {

	protected Class<?> testClass;
	protected Class<?> cls;

	public TestosteroneSuiteRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(Instrument.testClass(
				klass,
				new BeforeAnnotation(),
				new AfterAnnotation(),
				new BeforeClassAnnotation(),
				new AfterClassAnnotation()), builder);
		this.testClass = klass;
		cls = Instrument.testClass(klass, null, null, null, null);
	}

	protected Statement withBeforeClasses(Statement statement) {
		List<FrameworkMethod> befores = new TestClass(cls).getAnnotatedMethods(BeforeClass.class);
		List<FrameworkMethod> newBefores = new ArrayList<>();
		for (FrameworkMethod before : befores) {
			if (before.getDeclaringClass() == this.cls) {
				newBefores.add(0, before);
			} else {
				newBefores.add(before);
			}
		}
		return befores.isEmpty() ? statement : new RunBefores(statement, newBefores, null);
	}

	protected Statement withAfterClasses(Statement statement) {
		List<FrameworkMethod> afters = new TestClass(cls).getAnnotatedMethods(AfterClass.class);
		List<FrameworkMethod> newAfters = new ArrayList<>();
		for (FrameworkMethod after : afters) {
			if (after.getDeclaringClass() == this.cls) {
				newAfters.add(after);
			} else {
				newAfters.add(0, after);
			}
		}
		return afters.isEmpty() ? statement : new RunAfters(statement, newAfters, null);
	}

}
