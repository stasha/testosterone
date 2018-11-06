package info.stasha.testosterone.junit5;

import java.lang.annotation.Annotation;
import org.junit.jupiter.api.AfterAll;

/**
 * @AfterAll annotation implementation.
 * 
 * @author stasha
 */
public class AfterAllAnnotation implements AfterAll {

	@Override
	public Class<? extends Annotation> annotationType() {
		return AfterAll.class;
	}

}
