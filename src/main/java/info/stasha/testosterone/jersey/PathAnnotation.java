package info.stasha.testosterone.jersey;

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
		// returns random value in @Path annotation - @Path(randomnumber)
		return path == null ? String.valueOf(Math.random()).replace(".", "") : path;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Path.class;
	}

}
