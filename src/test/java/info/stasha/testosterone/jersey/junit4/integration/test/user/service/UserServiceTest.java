package info.stasha.testosterone.jersey.junit4.integration.test.user.service;

import info.stasha.testosterone.jersey.FactoryUtils;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.jersey.junit4.integration.app.user.User;
import info.stasha.testosterone.jersey.junit4.integration.app.user.dao.UserDao;
import info.stasha.testosterone.jersey.junit4.integration.app.user.dao.UserDaoFactory;
import info.stasha.testosterone.jersey.junit4.integration.app.user.service.UserService;
import info.stasha.testosterone.jersey.junit4.integration.app.user.service.UserServiceFactory;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class UserServiceTest implements Testosterone {

    @Context
    private UserService userService;

    @Context
    private UserDao userDao;

    private final User user = new User(1L, "New User", "New User Description", 22);

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(UserServiceFactory.class).to(UserService.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @Override
    public void configureMocks(AbstractBinder binder) {
        binder.bindFactory(FactoryUtils.<UserDao>mock(UserDaoFactory.class)).to(UserDao.class).in(Singleton.class);
    }

    public <T> T verify(T mock, int invocations) {
        return Mockito.verify(mock, times(invocations));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTest() throws Exception {
        userService.createUser(new User(1L));
    }

    @Test(expected = NullPointerException.class)
    public void createTest2() throws Exception {
        userService.createUser(null);
    }

    @Test
    public void createTest3() throws Exception {
        userService.createUser(user.setId(null));
        verify(userDao, 1).createUser(user.setId(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void readTest() throws Exception {
        userService.getUser(new User());
    }

    @Test(expected = NullPointerException.class)
    public void readTest2() throws Exception {
        userService.getUser(null);
    }

    @Test
    public void readTest3() throws Exception {
        userService.getUser(user);
        Mockito.verify(userDao, times(1)).getUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTest() throws Exception {
        userService.updateUser(new User());
    }

    @Test(expected = NullPointerException.class)
    public void updateTest2() throws Exception {
        userService.updateUser(null);
    }

    @Test
    public void updateTest3() throws Exception {
        userService.updateUser(user);
        Mockito.verify(userDao, times(1)).updateUser(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTest() throws Exception {
        userService.deleteUser(new User());
    }

    @Test(expected = NullPointerException.class)
    public void deleteTest2() throws Exception {
        userService.deleteUser(null);
    }

    @Test
    public void deleteTest3() throws Exception {
        userService.deleteUser(user);
        Mockito.verify(userDao, times(1)).deleteUser(user);
    }

}
