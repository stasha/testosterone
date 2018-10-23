package info.stasha.testosterone.annotation;

import java.lang.annotation.Annotation;
import javax.ws.rs.Path;

/**
 * Implementation of Path annotation.
 *
 * @author stasha
 */
public class PathAnnotation implements Path {

	@Override
	public String value() {
		return String.valueOf(Math.random()).replace(".", "");
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Path.class;
	}

}
