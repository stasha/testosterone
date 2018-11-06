package info.stasha.testosterone.junit5;

import java.lang.annotation.Annotation;
import org.junit.jupiter.api.AfterEach;

/**
 * @AfterEach annotation implementation.
 * 
 * @author stasha
 */
public class AfterEachAnnotation implements AfterEach {

	@Override
	public Class<? extends Annotation> annotationType() {
		return AfterEach.class;
	}

}
