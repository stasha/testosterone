package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Utils;
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

    public PathAnnotation(Class<?> clazz) {
        Path p = Utils.getAnnotation(clazz, Path.class);
        if (p != null) {
            this.path = p.value();
        } else {
            this.path = "";
        }
    }

    @Override
    public String value() {
        // returns random value in @Path annotation - @Path(randomnumber)
        return path == null ? "__generic__" + String.valueOf(Math.random()).replace(".", "") : path;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Path.class;
    }

}
