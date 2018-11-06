package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import org.junit.Before;

/**
 * @Before annotation implementation.
 * 
 * @author stasha
 */
public class BeforeAnnotation implements Before {

	@Override
	public Class<? extends Annotation> annotationType() {
		return Before.class;
	}

}
