package info.stasha.testosterone.junit5;

import java.lang.annotation.Annotation;
import org.junit.jupiter.api.BeforeEach;

/**
 * @BeforeEach annotation implementation.
 *
 * @author stasha
 */
public class BeforeEachAnnotation implements BeforeEach {

	@Override
	public Class<? extends Annotation> annotationType() {
		return BeforeEach.class;
	}

}
