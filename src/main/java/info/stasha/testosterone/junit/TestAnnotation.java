package info.stasha.testosterone.junit;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Implementation of Test annotation.
 *
 * @author stasha
 */
public class TestAnnotation implements Test {

	@Override
	public Class<? extends Annotation> annotationType() {
		return Test.class;
	}

	@Override
	public Class<? extends Throwable> expected() {
		return null;
	}

	@Override
	public long timeout() {
		return 0L;
	}

}
