package info.stasha.testosterone.junit4.integration.app.user.dao;

import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;

/**
 *
 * @author stasha
 */
public class UserDaoFactory implements Factory<UserDao> {

	@Context
	private ServiceLocator locator;

	@Override
	public UserDao provide() {
		return locator.createAndInitialize(UserDaoImpl.class);
	}

	@Override
	public void dispose(UserDao instance) {

	}
}
