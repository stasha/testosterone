package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import org.junit.BeforeClass;

/**
 * @AfterClass annotation implementation.
 *
 * @author stasha
 */
public class BeforeClassAnnotation implements BeforeClass {

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return BeforeClass.class;
	}

}
