package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import org.junit.AfterClass;

/**
 * @AfterClass annotation implementation.
 * 
 * @author stasha
 */
public class AfterClassAnnotation implements AfterClass {

	@Override
	public Class<? extends Annotation> annotationType() {
		return AfterClass.class;
	}

}
