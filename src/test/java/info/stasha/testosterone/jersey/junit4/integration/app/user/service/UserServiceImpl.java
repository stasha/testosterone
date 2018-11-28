package info.stasha.testosterone.jersey.junit4.integration.app.user.service;


import info.stasha.testosterone.jersey.junit4.integration.app.user.service.*;
import info.stasha.testosterone.jersey.junit4.integration.app.user.User;
import info.stasha.testosterone.jersey.junit4.integration.app.user.dao.UserDao;

import java.util.List;
import javax.ws.rs.core.Context;

/**
 *
 * @author stasha
 */
public class UserServiceImpl implements UserService {

    @Context
    private UserDao userDao;

    @Override
    public List<User> getAllUsers() throws Exception {
        return userDao.getAllUsers();
    }

    @Override
    public void createUser(User user) throws Exception {
        if (user.getId() != null) {
            throw new IllegalArgumentException("You can't create user with existing ID");
        }
        userDao.createUser(user);
    }

    @Override
    public User getUser(User user) throws Exception {
        if (user.getId() == null) {
            throw new IllegalArgumentException("userid must not be null");
        }
        return userDao.getUser(user);
    }

    @Override
    public void updateUser(User user) throws Exception {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User with null id can't be updated");
        }

        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(User user) throws Exception {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User with null id can't be deleted");
        }

        userDao.deleteUser(user);
    }

}
