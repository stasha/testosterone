package info.stasha.testosterone.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for opening input stream.
 *
 * @author stasha
 */
@Inherited
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadFile {

    /**
     * Path to the file. Path always starts from root of "classpath", so it can't be
     * used for loading files from file system.<br>
     * Injected result may be InputStream or String.
     *
     * @return
     */
    String value();
}
