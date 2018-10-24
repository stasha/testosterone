package info.stasha.testosterone.service;

import info.stasha.testosterone.jersey.FactorySupplier;

/**
 * Service factory
 *
 * @author stasha
 */
public class ServiceFactory implements FactorySupplier<Service> {


	@Override
	public Service get() {
		return new ServiceImpl();
	}

	@Override
	public Service provide() {
		return new ServiceImpl();
	}

	@Override
	public void dispose(Service t) {
		//do nothing
	}

}
