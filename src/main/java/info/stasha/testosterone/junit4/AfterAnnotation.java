package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import org.junit.After;

/**
 * @After annotation implementation.
 * 
 * @author stasha
 */
public class AfterAnnotation implements After {

	@Override
	public Class<? extends Annotation> annotationType() {
		return After.class;
	}

}
