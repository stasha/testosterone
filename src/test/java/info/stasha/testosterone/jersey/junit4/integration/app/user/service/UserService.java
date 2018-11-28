package info.stasha.testosterone.jersey.junit4.integration.app.user.service;

import info.stasha.testosterone.jersey.junit4.integration.app.user.User;
import java.util.List;

/**
 *
 * @author stasha
 */
public interface UserService {

    List<User> getAllUsers() throws Exception;

    void createUser(User user) throws Exception;

    User getUser(User user) throws Exception;

    void updateUser(User user) throws Exception;

    void deleteUser(User user) throws Exception;

}
