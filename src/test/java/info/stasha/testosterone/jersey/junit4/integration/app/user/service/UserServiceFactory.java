package info.stasha.testosterone.jersey.junit4.integration.app.user.service;

import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;

/**
 *
 * @author stasha
 */
public class UserServiceFactory implements Factory<UserService> {

    @Context
    private ServiceLocator locator;

    @Override
    public UserService provide() {
        return locator.createAndInitialize(UserServiceImpl.class);
    }

    @Override
    public void dispose(UserService instance) {

    }
}
