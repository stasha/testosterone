package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import org.junit.Before;

/**
 *
 * @author stasha
 */
public class BeforeAnnotation implements Before {

	@Override
	public Class<? extends Annotation> annotationType() {
		return Before.class;
	}

}
