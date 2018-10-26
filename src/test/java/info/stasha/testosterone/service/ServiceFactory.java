package info.stasha.testosterone.service;

import org.glassfish.hk2.api.Factory;

/**
 * Service factory
 *
 * @author stasha
 */
public class ServiceFactory implements Factory<Service> {


	@Override
	public Service provide() {
		return new ServiceImpl();
	}

	@Override
	public void dispose(Service t) {
		//do nothing
	}

}
