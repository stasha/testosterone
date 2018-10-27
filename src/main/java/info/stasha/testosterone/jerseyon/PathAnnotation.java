package info.stasha.testosterone.jerseyon;

import java.lang.annotation.Annotation;
import javax.ws.rs.Path;

/**
 * Implementation of Path annotation.
 *
 * @author stasha
 */
public class PathAnnotation implements Path {

	private String path;

	public PathAnnotation() {
	}

	public PathAnnotation(String path) {
		this.path = path;
	}

	@Override
	public String value() {
		return path == null ? String.valueOf(Math.random()).replace(".", "") : path;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Path.class;
	}

}
