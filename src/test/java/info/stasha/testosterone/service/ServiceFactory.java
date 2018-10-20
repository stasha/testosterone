package info.stasha.testosterone.service;

import java.util.function.Supplier;

/**
 * Service factory
 *
 * @author stasha
 */
public class ServiceFactory implements Supplier<Service> {

	@Override
	public Service get() {
		return new ServiceImpl();
	}

}
