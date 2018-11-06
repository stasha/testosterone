package info.stasha.testosterone.junit5;

import java.lang.annotation.Annotation;
import org.junit.jupiter.api.BeforeAll;

/**
 * @BeforeAll annotation implementation.
 * 
 * @author stasha
 */
public class BeforeAllAnnotation implements BeforeAll {

	@Override
	public Class<? extends Annotation> annotationType() {
		return BeforeAll.class;
	}

}
