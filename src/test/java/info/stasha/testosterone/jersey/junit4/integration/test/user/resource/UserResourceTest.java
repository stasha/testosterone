package info.stasha.testosterone.jersey.junit4.integration.test.user.resource;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.FactoryUtils;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.jersey.junit4.integration.app.user.User;
import info.stasha.testosterone.jersey.junit4.integration.app.user.UserResource;
import info.stasha.testosterone.jersey.junit4.integration.app.user.service.UserService;
import info.stasha.testosterone.jersey.junit4.integration.app.user.service.UserServiceFactory;
import javax.inject.Singleton;
import static javax.ws.rs.HttpMethod.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Testing UserResource with mocks.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class UserResourceTest implements Testosterone {

    protected final User user = new User(3L, "Title", "Description", 11);
    protected final User createUser = new User("Title", "Description", 45);
    public Entity userEntity = Entity.json(user);
    public Entity createUserEntity = Entity.json(createUser);

    private final UserResource userResource = Mockito.spy(new UserResource());

    @Context
    protected UserService userService;

    @Override
    public void configure(ResourceConfig config) {
        config.register(userResource);
    }

    @Override
    public void configureMocks(AbstractBinder binder) {
        binder.bindFactory(FactoryUtils.<UserService>mock(UserServiceFactory.class)).to(UserService.class).in(Singleton.class);
    }

    public <T> T verify(T mock, int invocations) {
        return Mockito.verify(mock, times(invocations));
    }

    @Test
    @Request(url = "user", method = GET, expectedStatus = {200, 204})
    public void getAllUsers(Response resp) throws Exception {
        verify(userResource, 1).getAllUsers();
        verify(userService, 1).getAllUsers();
    }

    @Test
    @Request(url = "user", method = POST, entity = "userEntity", expectedStatus = {200, 204})
    public void createUser(Response resp) throws Exception {
        verify(userResource, 1).createUser(Matchers.refEq(user));
        verify(userService, 1).createUser(Matchers.refEq(user));
    }

    @Test
    @Request(url = "user/1", method = GET, expectedStatus = {200, 204})
    public void getUser(Response resp) throws Exception {
        verify(userResource, 1).getUser(any(User.class));
        verify(userService, 1).getUser(any(User.class));
    }

    @Test
    @Request(url = "user/1", method = PUT, entity = "userEntity", expectedStatus = {200, 204})
    public void updateUser(Response resp) throws Exception {
        verify(userResource, 1).updateUser(Matchers.refEq(user));
        verify(userService, 1).updateUser(Matchers.refEq(user));
    }

    @Test
    @Request(url = "user/1", method = DELETE, expectedStatus = {200, 204})
    public void deleteUser(Response resp) throws Exception {
        verify(userResource, 1).deleteUser(any(User.class));
        verify(userService, 1).deleteUser(any(User.class));
    }

}
