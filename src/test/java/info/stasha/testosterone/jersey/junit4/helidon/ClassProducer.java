package info.stasha.testosterone.jersey.junit4.helidon;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

/**
 *
 * @author stasha
 */
@ApplicationScoped
public class ClassProducer {

    public static class ProducedClass {

        public String getMessage() {
            return "message";
        }
    }

    @Produces
    @Dependent
    public ProducedClass getProducedClass() {
        return new ProducedClass();
    }

    public void dispose(@Disposes ProducedClass l) {
        System.out.println("a");
    }
}
