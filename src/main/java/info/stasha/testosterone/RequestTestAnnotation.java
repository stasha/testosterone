package info.stasha.testosterone;

import java.lang.annotation.Annotation;

/**
 * Implementation of RequestTest annotation.
 *
 * @author stasha
 */
public class RequestTestAnnotation implements RequestTest {

	@Override
	public Class<? extends Annotation> annotationType() {
		return RequestTest.class;
	}

	@Override
	public Class<? extends Throwable> expected() {
		return null;
	}

}
