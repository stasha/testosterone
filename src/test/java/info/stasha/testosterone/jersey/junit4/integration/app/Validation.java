package info.stasha.testosterone.jersey.junit4.integration.app;

import javax.validation.groups.Default;

/**
 *
 * @author stasha
 */
public class Validation {

    public static interface Create extends Default {
    }

    public static interface Read extends Default {
    }

    public static interface Update extends Default {
    }

    public static interface Delete extends Default {
    }
}
