package info.stasha.testosterone.jersey;

import org.glassfish.hk2.api.Factory;
import java.util.function.Supplier;

/**
 * Support for Factory and Supplier for older and newer Jersey versions.
 *
 * @author stasha
 * @param <T>
 */
public interface FactorySupplier<T> extends Factory<T>, Supplier<T> {

}
