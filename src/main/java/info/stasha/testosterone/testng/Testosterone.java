package info.stasha.testosterone.testng;

import info.stasha.testosterone.TestInstrumentation;
import info.stasha.testosterone.Utils;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.IObjectFactory2;
import org.testng.ITestObjectFactory;
import org.testng.annotations.ObjectFactory;

/**
 * TestNG Testosterone
 *
 * @author stasha
 */
public interface Testosterone extends info.stasha.testosterone.jersey.Testosterone {

    @ObjectFactory
    default ITestObjectFactory getFactory() {
        return new TestObjectFactory();
    }

    /**
     * Factory for creating new test class instances.
     *
     * @return
     */
    public static class TestObjectFactory implements IObjectFactory2 {

        @Override
        public Object newInstance(Class<?> cls) {
            try {
                if (!Utils.isTestosterone(cls)) {
                    return cls.newInstance();
                }
                
                return TestInstrumentation.testClass((Class<? extends Testosterone>) cls,
                        new BeforeClassAnnotation(), new AfterClassAnnotation()).newInstance();
            } catch (Throwable ex) {
                Logger.getLogger(TestObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }

    }

}
