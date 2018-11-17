package info.stasha.testosterone;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Junit4, Junit5 and TestNG annotations.
 *
 * @author stasha
 */
public class TestAnnotations {

    /**
     * Tries to load class from passed className list.
     *
     * @param className
     * @return
     */
    private static List<Class<? extends Annotation>> get(String... className) {
        List<Class<? extends Annotation>> annotations = new ArrayList<>();
        for (String name : className) {
            try {
                annotations.add((Class<? extends Annotation>) Class.forName(name));
            } catch (ClassNotFoundException ex) {
                // do nothing
            }
        }
        if (annotations.isEmpty()) {
            throw new IllegalStateException("You are missing testing library. Add JUnit4, JUnit5 or TestNG to project.");
        }
        return annotations;
    }

    /**
     * Returns list of available annotations based on passed string.
     *
     * @param annotation
     * @return
     */
    private static List<Class<? extends Annotation>> getAnnotation(String annotation) {
        switch (annotation) {
            case "beforeClass":
                return get("org.junit.BeforeClass", "org.junit.jupiter.api.BeforeAll", "org.testng.annotations.BeforeClass");
            case "before":
                return get("org.junit.Before", "org.junit.jupiter.api.BeforeEach", "org.testng.annotations.BeforeMethod");
            case "test":
                return get("org.junit.Test", "org.junit.jupiter.api.Test", "org.testng.annotations.Test");
            case "after":
                return get("org.junit.After", "org.junit.jupiter.api.AfterEach", "org.testng.annotations.AfterMethod");
            case "afterClass":
                return get("org.junit.AfterClass", "org.junit.jupiter.api.AfterAll", "org.testng.annotations.AfterClass");
        }
        return null;
    }

    public static final List<Class<? extends Annotation>> BEFORECLASS = getAnnotation("beforeClass");
    public static final List<Class<? extends Annotation>> BEFORE = getAnnotation("before");
    public static final List<Class<? extends Annotation>> TEST = getAnnotation("test");
    public static final List<Class<? extends Annotation>> AFTER = getAnnotation("after");
    public static final List<Class<? extends Annotation>> AFTERCLASS = getAnnotation("afterClass");

}
