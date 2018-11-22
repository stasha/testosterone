package info.stasha.testosterone.jersey.junit4.jersey.injectables;

import info.stasha.testosterone.annotation.LoadFile;

/**
 *
 * @author stasha
 */
public class LoadFileNotExistingPath {

    @LoadFile("fdsaf")
    private String b;
}
