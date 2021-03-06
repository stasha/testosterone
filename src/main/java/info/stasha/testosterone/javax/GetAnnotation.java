package info.stasha.testosterone.javax;

import java.lang.annotation.Annotation;
import javax.ws.rs.GET;

/**
 * Implementation of GET annotation
 *
 * @author stasha
 */
public class GetAnnotation implements GET {

	@Override
	public Class<? extends Annotation> annotationType() {
		return GET.class;
	}
}
