package info.stasha.testosterone;

import java.lang.annotation.Annotation;
import javax.ws.rs.GET;

/**
 *
 * @author stasha
 */
public class GetAnnotation implements GET {

	@Override
	public Class<? extends Annotation> annotationType() {
		return GET.class;
	}
}
