package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import org.junit.BeforeClass;

/**
 * @BeforeClass annotation implementation.
 * 
 * @author stasha
 */
public class BeforeClassAnnotation implements BeforeClass {

	@Override
	public Class<? extends Annotation> annotationType() {
		return BeforeClass.class;
	}

}
