package info.stasha.testosterone.jersey;

import java.lang.annotation.Annotation;
import javax.annotation.PostConstruct;

/**
 * @PostConstruct annotation implementation.
 *
 * @author stasha
 */
public class PostConstructAnnotation implements PostConstruct {

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return PostConstruct.class;
	}

}
