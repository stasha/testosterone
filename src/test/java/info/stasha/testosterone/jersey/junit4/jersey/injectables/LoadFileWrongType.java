package info.stasha.testosterone.jersey.junit4.jersey.injectables;

import info.stasha.testosterone.annotation.LoadFile;

/**
 *
 * @author stasha
 */
public class LoadFileWrongType {

    @LoadFile("fdsaf")
    private Boolean b;
}
