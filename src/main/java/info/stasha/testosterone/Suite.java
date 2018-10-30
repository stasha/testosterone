package info.stasha.testosterone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author stasha
 */
public class Suite {

	private final Set<Class<?>> suiteClasses = new LinkedHashSet<>();

	public Suite add(Class<?>... clazz) {
		suiteClasses.addAll(Arrays.asList(clazz));
		return this;
	}

	public List<Class<?>> getSuiteClasses() {
		List<Class<?>> reversed = new ArrayList(suiteClasses);
		Collections.reverse(reversed);
		return reversed;
	}
}
