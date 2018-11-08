package info.stasha.testosterone.junit4.jersey.service;

import org.glassfish.hk2.api.Factory;

/**
 * Service factory
 *
 * @author stasha
 */
public class ServiceFactory2 implements Factory<Service2> {


	@Override
	public Service2 provide() {
		return new ServiceImpl2();
	}

	@Override
	public void dispose(Service2 t) {
		//do nothing
	}

}
