package info.stasha.testosterone.jerseyon;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for storing test objects from main thread.<br>
 * "main" test object are created by JUnit. They are always created as first.
 *
 * @author stasha
 */
public class TestosteroneMain {

	private static final Map<Class<?>, TestosteroneMain> PAIRS = new HashMap<>();

	private Testosterone main;

	public static TestosteroneMain getMain(Testosterone obj) {
		TestosteroneMain m = PAIRS.get(obj.getClass());
		if (m == null) {
			m = new TestosteroneMain();
		}
		return m;
	}

	public static void removeMain(Testosterone obj) {
		PAIRS.remove(obj.getClass());
	}

	public Testosterone getMain() {
		return main;
	}

	public void setMain(Testosterone obj) {
		if (this.main == null) {
			this.main = obj;
			PAIRS.put(obj.getClass(), this);
		}
	}

}
