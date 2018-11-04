package info.stasha.testosterone;

import info.stasha.testosterone.jersey.Testosterone;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for storing test objects from main thread.<br>
 * "main" test object are created by JUnit. They are always created as first.
 *
 * @author stasha
 */
public class MainTest {

	private static final Map<String, MainTest> PAIRS = new HashMap<>();

	private Testosterone main;

	public static MainTest getMain(Testosterone obj) {
		MainTest m = PAIRS.get(obj.getClass().getName());
		if (m == null) {
			m = new MainTest();
		}
		return m;
	}
	
	public static MainTest getMain(Class<?> cls) {
		MainTest m = PAIRS.get(cls.getName());
		if (m == null) {
			m = new MainTest();
		}
		return m;
	}

	public static void removeMain(Testosterone obj) {
		PAIRS.remove(obj.getClass().getName());
	}
	
	public static void removeMain(Class<?> obj) {
		PAIRS.remove(obj.getName());
	}

	public Testosterone getMain() {
		return main;
	}

	public void setMain(Testosterone obj) {
		if (this.main == null) {
			this.main = obj;
			PAIRS.put(obj.getClass().getName(), this);
		}
	}

}
